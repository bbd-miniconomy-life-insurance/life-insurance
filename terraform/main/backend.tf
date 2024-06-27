terraform {
  backend "s3" {
    bucket         = "life-insurance-docker-image-1475"
    key            = "state/terraform.tfstate"
    region         = "eu-west-1"
    encrypt        = true
  }
}