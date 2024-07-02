resource "aws_vpc" "vpc" {
  cidr_block           = var.cidr_block
  enable_dns_hostnames = true
  tags                 = merge(var.mandatory_tags, { Name = "${var.project_name}-vpc" })
}

resource "aws_subnet" "public_subnets" {
  count             = length(var.vpc_public_subnets)
  vpc_id            = aws_vpc.vpc.id
  cidr_block        = var.vpc_public_subnets[count.index].cidr_block
  tags              = merge(var.mandatory_tags, { Name = "${var.project_name}-public-subnet-${count.index}" })
  availability_zone = var.vpc_public_subnets[count.index].az
}

resource "aws_subnet" "private_subnets" {
  count             = length(var.vpc_private_subnets)
  vpc_id            = aws_vpc.vpc.id
  cidr_block        = var.vpc_private_subnets[count.index].cidr_block
  availability_zone = var.vpc_private_subnets[count.index].az
  tags              = merge(var.mandatory_tags, { Name = "${var.project_name}-private-subnet-${count.index}" })
}

# Internet Gateway
resource "aws_internet_gateway" "internet_gateway" {
  vpc_id = aws_vpc.vpc.id
  tags   = merge(var.mandatory_tags, { Name = "${var.project_name}-internet-gateway" })
}

# Routing table
resource "aws_route_table" "route_table" {
  vpc_id = aws_vpc.vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gateway.id
  }
  tags = merge(var.mandatory_tags, { Name = "${var.project_name}-route-table" })
}

# Resource association table
resource "aws_route_table_association" "route_table_association" {
  count          = length(aws_subnet.public_subnets)
  subnet_id      = aws_subnet.public_subnets[count.index].id
  route_table_id = aws_route_table.route_table.id
}

# NAT Gateway Elastic IP
resource "aws_eip" "nat_eip" {
  domain = "vpc"
}

# NAT Gateway
resource "aws_nat_gateway" "nat_gateway" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public_subnets[0].id
  tags          = merge(var.mandatory_tags, { Name = "${var.project_name}-nat-gateway" })
}

# Private Routing table
resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway.id
  }
  tags = merge(var.mandatory_tags, { Name = "${var.project_name}-private-route-table" })
}

# Associate private subnets with the private route table
resource "aws_route_table_association" "private_route_table_association" {
  count          = length(aws_subnet.private_subnets)
  subnet_id      = aws_subnet.private_subnets[count.index].id
  route_table_id = aws_route_table.private_route_table.id
}

#Check this
resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "${var.project_name}-subnet-group"
  subnet_ids = aws_subnet.public_subnets[*].id
  tags       = merge(var.mandatory_tags, { Name = "${var.project_name}-public-subnet-group" })
}

resource "aws_db_instance" "db" {
  identifier                  = "${var.project_name}-db"
  allocated_storage           = 20
  engine                      = "postgres"
  engine_version              = "16"
  instance_class              = "db.t3.micro"
  publicly_accessible         = true
  username                    = "dbadmin"
  multi_az                    = false # Free tier supports only single AZ
  manage_master_user_password = true  #Fetch password from console
  apply_immediately           = true
  copy_tags_to_snapshot       = true
  db_subnet_group_name        = aws_db_subnet_group.db_subnet_group.name
  skip_final_snapshot         = true
  db_name                     = "miniconomy"

  vpc_security_group_ids = [
    aws_security_group.db_security_group.id
  ]
  tags = merge(var.mandatory_tags, { Name = "${var.project_name}-db" })
}

# API
resource "aws_elastic_beanstalk_application" "web_app" {
  name        = "${var.project_name}-web-app"
  description = "Beanstalk application"
}

resource "aws_elastic_beanstalk_environment" "web_env" {
  name                = "${var.project_name}-web-env"
  application         = aws_elastic_beanstalk_application.web_app.name
  solution_stack_name = "64bit Amazon Linux 2023 v4.2.5 running Corretto 21"
  cname_prefix        = "${var.project_name}-web"

  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = aws_vpc.vpc.id
  }
  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = join(",", aws_subnet.private_subnets[*].id)
  }
  setting {
    namespace = "aws:ec2:vpc"
    name      = "ELBSubnets"
    value     = join(",", aws_subnet.public_subnets[*].id)
  }
  setting {
    namespace = "aws:ec2:instances"
    name      = "InstanceTypes"
    value     = "t3.micro"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "2"
  }

  setting {
    namespace = "aws:elbv2:loadbalancer"
    name      = "IdleTimeout"
    value     = "60"
  }
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.ec2_instance_profile.name
  }
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_security_group_web.id
  }
  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "LoadBalancerType"
    value     = "application"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = aws_iam_role.eb_service_role.name
  }

  setting {
    namespace = "aws:elasticbeanstalk:healthreporting:system"
    name      = "SystemType"
    value     = "basic"
  }

  setting {
    namespace = "aws:elbv2:listener:443"
    name      = "Protocol"
    value     = "HTTPS"
  }

  setting {
    namespace = "aws:elbv2:listener:443"
    name      = "ListenerEnabled"
    value     = "true"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "DefaultProcess"
    value     = "default"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "Protocol"
    value     = "HTTP"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "ListenerEnabled"
    value     = "true"
  }

  setting {
    namespace = "aws:elbv2:loadbalancer"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_security_group_lb.id
  }

  setting {
    namespace = "aws:elbv2:listener:443"
    name      = "SSLCertificateArns"
    value     = "arn:aws:acm:eu-west-1:804180393465:certificate/d1865b28-a928-479b-abed-882f0c53a041" # Replace with your SSL certificate ARN
  }

  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "AWS_RDS_ENDPOINT"
    value     = var.AWS_RDS_ENDPOINT
  }

  setting {
    namespace   = "aws:elasticbeanstalk:application:environment"
    name        = "DB_USERNAME"
    value       = var.DB_USERNAME
  }

  setting {
    namespace   = "aws:elasticbeanstalk:application:environment"
    name        = "DB_PASSWORD"
    value       = var.DB_PASSWORD
  }

  setting {
    namespace   = "aws:elasticbeanstalk:application:environment"
    name        = "AWS_USER_POOL"
    value       = var.AWS_USER_POOL
  }

  setting {
    namespace   = "aws:elasticbeanstalk:application:environment"
    name        = "ISSUER_URI"
    value       = var.ISSUER_URI
  }

  setting {
    namespace   = "aws:elasticbeanstalk:application:environment"
    name        = "AUDIENCE"
    value       = var.AUDIENCE
  }

  setting {
    namespace   = "aws:elasticbeanstalk:application:environment"
    name        = "JWK_SET_URI"
    value       = var.JWK_SET_URI
  }
}

# FRONTEND
resource "aws_elastic_beanstalk_application" "ui_app" {
  name        = "${var.project_name}-ui-app"
  description = "Beanstalk application"
}

resource "aws_elastic_beanstalk_environment" "ui_env" {
  name                = "${var.project_name}-ui-env"
  application         = aws_elastic_beanstalk_application.ui_app.name
  solution_stack_name = "64bit Amazon Linux 2023 v6.1.4 running Node.js 20"
  cname_prefix        = "${var.project_name}-ui"

  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = aws_vpc.vpc.id
  }
  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = join(",", aws_subnet.private_subnets[*].id)
  }
  setting {
    namespace = "aws:ec2:vpc"
    name      = "ELBSubnets"
    value     = join(",", aws_subnet.public_subnets[*].id)
  }
  setting {
    namespace = "aws:ec2:instances"
    name      = "InstanceTypes"
    value     = "t3.micro"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "2"
  }

  setting {
    namespace = "aws:elbv2:loadbalancer"
    name      = "IdleTimeout"
    value     = "60"
  }
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.ec2_instance_profile.name
  }
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_security_group_web.id
  }
  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "LoadBalancerType"
    value     = "application"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = aws_iam_role.eb_service_role.name
  }

  setting {
    namespace = "aws:elasticbeanstalk:healthreporting:system"
    name      = "SystemType"
    value     = "basic"
  }

  setting {
    namespace = "aws:elbv2:listener:443"
    name      = "Protocol"
    value     = "HTTPS"
  }

  setting {
    namespace = "aws:elbv2:listener:443"
    name      = "ListenerEnabled"
    value     = "true"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "DefaultProcess"
    value     = "default"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "Protocol"
    value     = "HTTP"
  }

  setting {
    namespace = "aws:elbv2:listener:80"
    name      = "ListenerEnabled"
    value     = "true"
  }

  setting {
    namespace = "aws:elbv2:loadbalancer"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_security_group_lb.id
  }

  setting {
    namespace = "aws:elbv2:listener:443"
    name      = "SSLCertificateArns"
    value     = "arn:aws:acm:eu-west-1:804180393465:certificate/5ee45d38-ba76-4c13-a91f-614d4d872345" # Replace with your SSL certificate ARN
  }

}

#mTLS

#API Gateway
resource "aws_apigatewayv2_api" "api" {
  name          = "${var.project_name}-service-api"
  description   = "API Gateway for the service endpoints"
  protocol_type = "HTTP"


  cors_configuration {
    allow_credentials = true
    allow_headers     = ["Content-Type", "Authorization"]
    allow_methods     = ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
    allow_origins     = var.cors_configuration
    max_age           = 3000
  }
}

resource "aws_apigatewayv2_domain_name" "api" {
  domain_name = "api.life.projects.bbdgrad.com"


  domain_name_configuration {
    certificate_arn = "arn:aws:acm:eu-west-1:804180393465:certificate/d1865b28-a928-479b-abed-882f0c53a041"
    endpoint_type   = "REGIONAL"
    security_policy = "TLS_1_2"
  } 
  mutual_tls_authentication {
    truststore_uri = "s3://miniconomy-trust-store-bucket/truststore.pem"
  }
}

resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.api.id
  name        = "$default"
  auto_deploy = true
}


resource "aws_apigatewayv2_api_mapping" "api" {
  api_id      = aws_apigatewayv2_api.api.id
  domain_name = aws_apigatewayv2_domain_name.api.domain_name
  stage       = aws_apigatewayv2_stage.default.id
}

#Load balancer
resource "aws_lb" "api_lb" {
  name               = "${var.project_name}-api-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.lb_sg.id]
  subnets            = aws_subnet.public_subnets[*].id
}

resource "aws_lb_target_group" "api_lb_tg" {
  name     = "${var.project_name}-api-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = aws_vpc.vpc.id

  health_check {
    path                = "/"
    protocol            = "HTTP"
    port                = "traffic-port"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 5
    unhealthy_threshold = 2
    matcher             = "200-299"
  }
}


# resource "aws_lb_listener" "api_lb_listener" {
#   load_balancer_arn = aws_lb.api_lb.arn
#   port              = "80"
#   protocol          = "HTTP"

#   default_action {
#     type             = "forward"
#     target_group_arn = aws_lb_target_group.api_lb_tg.arn
#   }

#   mutual_authentication {
#     mode            = "verify"
#     trust_store_arn = "arn:aws:acm:eu-west-1:804180393465:certificate/d1865b28-a928-479b-abed-882f0c53a041"
#   }
# }

resource "aws_lb_listener" "api_lb_listener" {
  load_balancer_arn = aws_lb.api_lb.arn
  port              = 443
  protocol          = "HTTPS"

  ssl_policy        = "ELBSecurityPolicy-2016-08"

  certificate_arn   = "arn:aws:acm:eu-west-1:804180393465:certificate/d1865b28-a928-479b-abed-882f0c53a041"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api_lb_tg.arn
  }

  mutual_authentication {
    mode            = "verify"
    trust_store_arn = "arn:aws:elasticloadbalancing:eu-west-1:804180393465:truststore/lie-truststore/f873543a3ca00331"
  }
}









