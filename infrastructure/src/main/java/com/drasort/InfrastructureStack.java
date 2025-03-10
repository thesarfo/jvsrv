package com.drasort;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
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
    }
}
