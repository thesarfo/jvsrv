package com.drasort;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.apigateway.CorsOptions;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


        var dragonTable = Table.Builder.create(this, "DragonTable")
                .tableName("Dragons")
                .partitionKey(Attribute.builder()
                        .name("id")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        var dragonFunction = Function.Builder.create(this, "DragonResortApp")
                .functionName("DragonResort")
                .description("Application to handle our guest in our Dragon Resort")
                .runtime(Runtime.NODEJS_22_X)
                .handler("index.handler")
                .code(Code.fromInline(
                        "exports.handler = async function(event, context) {\n" +
                                "  return {\n" +
                                "    statusCode: 501,\n" +
                                "    headers: {\n" +
                                "      'Content-Type': 'application/json'\n" +
                                "    },\n" +
                                "    body: JSON.stringify({\n" +
                                "      message: 'Sorry, no Dragons allowed in our 5 STAR Resort!'\n" +
                                "    })\n" +
                                "  };\n" +
                                "}"
                ))
                .timeout(Duration.seconds(30))
                .memorySize(128)
                .build();

        dragonTable.grantReadWriteData(dragonFunction);

        var api = RestApi.Builder.create(this, "DragonApi")
                .restApiName("Dragon Resort API")
                .description("Serverless Dragon Resort API")
                .defaultCorsPreflightOptions(CorsOptions.builder()
                        .allowOrigins(List.of("*"))
                        .allowMethods(List.of("GET", "POST", "PUT", "DELETE")).build())
                .build();

        var lambdaIntegration = new LambdaIntegration(dragonFunction);


        // Add resources and methods for CRUD operations
        var dragons = api.getRoot().addResource("dragons");
        dragons.addMethod("POST", lambdaIntegration);
        dragons.addMethod("GET", lambdaIntegration);

        var dragon = dragons.addResource("{id}");
        dragon.addMethod("GET", lambdaIntegration);
        dragon.addMethod("PUT", lambdaIntegration);
        dragon.addMethod("DELETE", lambdaIntegration);
    }
}
