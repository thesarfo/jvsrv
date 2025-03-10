# For each curl command, replace <apigatewayid.execute-api.eu-central-1.amazonaws.com/prod> with the correct from your
# environment

# POST one dragon
curl -X POST https://<apigatewayid.execute-api.eu-central-1.amazonaws.com/prod>/dragons \
-H 'Content-Type: application/json' \
-d '{
        "name": "Draco",
        "type": "Celestial Dragon",
        "age": "3000",
        "color": "bronze"
    }'

# GET all dragons
curl -X GET https://<apigatewayid>.execute-api.eu-central-1.amazonaws.com/prod/dragons \
-H 'Content-Type: application/json'

# GET one dragon | Replace <id> with a UUID you could see in the list of dragons
curl -X GET https://<apigatewayid>.execute-api.eu-central-1.amazonaws.com/prod/dragons/<id> \
-H 'Content-Type: application/json'

# DELETE one dragon | Replace <id> with a UUID you could see in the list of dragons
curl -X DELETE https://<apigatewayid>.execute-api.eu-west-1.amazonaws.com/prod/dragons/<id>