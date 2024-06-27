mandatory_tags = {
  owner         = "cameron.zwane@bbd.co.za"
  created-using = "terraform"
}

account_number = "804180393465"

# backend_certificate = "d1865b28-a928-479b-abed-882f0c53a041" - this later

# frontend_certificate = "d1865b28-a928-479b-abed-882f0c53a041" - this later

repo = "bbd-miniconomy-life-insurance/life-insurance"

region = "eu-west-1"

project_name = "life-insurance"

vpc_public_subnets = [
  {
    cidr_block = "15.0.1.0/24"
    az         = "eu-west-1a"
  },
  {
    cidr_block = "15.0.3.0/24"
    az         = "eu-west-1b"
}]

vpc_private_subnets = [
  {
    cidr_block = "15.0.5.0/24"
    az         = "eu-west-1a"
  },
  {
    cidr_block = "15.0.7.0/24"
    az         = "eu-west-1b"
}]

db_port = 1433

eb_port_web = 80

