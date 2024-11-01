#!/usr/bin/env bash

azd env set AZURE_RESOURCE_GROUP $resourceGroupName

azd env set AZURE_CONTAINER_REGISTRY_ENDPOINT $acrLoginServer

azd env set AZD_PROVISION_TIMESTAMP $azdProvisionTimestamp

GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo ""
echo -e "${GREEN}INFO:${NC} Deploy finish succeed!"

echo -e "${GREEN}INFO:${NC} Api Gateway App url: https://$appFqdn"