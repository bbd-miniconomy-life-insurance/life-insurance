# resource "aws_cognito_user_pool" "pool" {
#   name = "life-insurance-user-pool"
# }
#
# resource "aws_cognito_user_pool_domain" "main" {
#   domain       = "bbd-miniconomy-life-insurance"
#   user_pool_id = aws_cognito_user_pool.pool.id
# }
#
# resource "aws_cognito_identity_provider" "google" {
#   user_pool_id  = aws_cognito_user_pool.pool.id
#   provider_name = "Google"
#   provider_type = "Google"
#
#   provider_details = {
#     authorize_scopes = "profile email openid"
#     client_id        = local.google_secret["google_client_id"]
#     client_secret    = local.google_secret["google_client_secret"]
#   }
#
#   attribute_mapping = {
#     email    = "email"
#     username = "sub"
#     name     = "name"
#     picture  = "picture"
#   }
#
#   lifecycle {
#     ignore_changes = [ provider_details ]
#   }
# }
#
# resource "aws_cognito_user_pool_client" "api_client" {
#   name         = "life-insurance-client"
#   user_pool_id = aws_cognito_user_pool.pool.id
#
#   callback_urls = ["https://life.projects.bbdgrad.com/login"]
#
#   generate_secret = true
#
#   allowed_oauth_flows_user_pool_client = true
#   allowed_oauth_flows                  = ["code"]
#   allowed_oauth_scopes                 = ["email", "openid", "profile"]
#   supported_identity_providers         = ["Google"]
# }
#
# resource "aws_cognito_user_pool_client" "api_client_local" {
#   name         = "life-insurance-client-local"
#   user_pool_id = aws_cognito_user_pool.pool.id
#
#   callback_urls = ["http://localhost:4200/login"]
#
#   generate_secret = true
#
#   allowed_oauth_flows_user_pool_client = true
#   allowed_oauth_flows                  = ["code"]
#   allowed_oauth_scopes                 = ["email", "openid", "profile"]
#   supported_identity_providers         = ["Google"]
# }
#
# # TODO: ASK JOSH
# data "aws_secretsmanager_secret_version" "google_oauth" {
#   secret_id = "arn:aws:secretsmanager:eu-west-1:958933916692:secret:google_oauth/client_id_and_secret-hhzgyQ"
# }
#
# locals {
#   google_secret = jsondecode(data.aws_secretsmanager_secret_version.google_oauth.secret_string)
# }
