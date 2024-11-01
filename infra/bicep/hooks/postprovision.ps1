azd env set AZURE_RESOURCE_GROUP $env:resourceGroupName

azd env set AZURE_CONTAINER_REGISTRY_ENDPOINT $env:acrLoginServer

azd env set AZD_PROVISION_TIMESTAMP $env:azdProvisionTimestamp

Write-Host ""
Write-Host "INFO: " -ForegroundColor Green -NoNewline;
Write-Host "Deploy finish succeed!"

Write-Host "INFO: " -ForegroundColor Green -NoNewline;
Write-Host "Api Gateway App url: https://$env:appFqdn"