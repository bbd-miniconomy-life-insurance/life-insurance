# Create a security group so that client can connect to the database
resource "aws_security_group" "db_security_group" {
  name        = "${var.project_name}-db-security_group"
  description = "Security group for db"
  vpc_id      = aws_vpc.vpc.id

  #Enable internet access to the database
  # Allow inbound traffic from IPv4
  ingress {
    from_port   = var.db_port
    to_port     = var.db_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Allow inbound traffic from any IPv4 address
  }

  # Allow inbound traffic from IPv6
  ingress {
    from_port        = var.db_port
    to_port          = var.db_port
    protocol         = "tcp"
    ipv6_cidr_blocks = ["::/0"] # Allow inbound traffic from any IPv6 address
  }

  # Define outbound rule to allow traffic to the EC2 instance from the RDS database
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.mandatory_tags, { Name = "${var.project_name}-db-security-group" })

}

# Define security group for the EC2 instance
resource "aws_security_group" "eb_security_group_web" {
  vpc_id      = aws_vpc.vpc.id
  name        = "${var.project_name}-eb-security-group-web"
  description = "Security group for eb"

  # Define inbound rules to allow traffic from anywhere to the EC2 instance on port 8080 (for Spring Boot)
  ingress {
    from_port   = var.eb_port_web
    to_port     = var.eb_port_web
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Define inbound rules to allow traffic from anywhere to the EC2 instance on port 22 (for SSH)
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Define outbound rules to allow the EC2 instance to communicate with the RDS database
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.mandatory_tags, { Name = "${var.project_name}-eb-security-group-web" })
}

resource "aws_security_group" "eb_security_group_lb" {
  vpc_id      = aws_vpc.vpc.id
  name        = "${var.project_name}-eb-security-group-lb"
  description = "Security group for lb"

  # Define inbound rules to allow traffic from anywhere to the EC2 instance on port 8080 (for Spring Boot)
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = var.eb_port_web
    to_port     = var.eb_port_web
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Define inbound rules to allow traffic from anywhere to the EC2 instance on port 22 (for SSH)
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # ingress {
  #   from_port   = 443
  #   to_port     = 443
  #   protocol    = "tcp"
  #   cidr_blocks = ["0.0.0.0/0"]
  # }

  # Define outbound rules to allow the EC2 instance to communicate with the RDS database
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.mandatory_tags, { Name = "${var.project_name}-eb-security-group-lb" })
}


# Security group for the load balancer
resource "aws_security_group" "lb_sg" {
  vpc_id      = aws_vpc.vpc.id
  name        = "${var.project_name}-security-group-lb"
  description = "Allow HTTPS inbound traffic"

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}