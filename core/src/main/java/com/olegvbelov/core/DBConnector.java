package com.olegvbelov.core;

import com.yandex.ydb.auth.iam.CloudAuthProvider;
import com.yandex.ydb.core.auth.AuthProvider;
import com.yandex.ydb.core.grpc.GrpcTransport;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.SessionRetryContext;
import com.yandex.ydb.table.TableClient;
import com.yandex.ydb.table.rpc.grpc.GrpcTableRpc;
import yandex.cloud.sdk.auth.provider.CredentialProvider;
import yandex.cloud.sdk.auth.provider.OauthCredentialProvider;

public class DBConnector {
    private final String database;
    private final String oauthToken;
    private final String endpoint;

    public DBConnector() {

        this.database = System.getenv("DATABASE");
        this.oauthToken = System.getenv("YC_TOKEN");
        this.endpoint = System.getenv("ENDPOINT");
    }

    public Session connect() {

            return getTableClient().createSession()
                    .join()
                    .expect("cannot create session");
    }

    public SessionRetryContext getContext() {
        return SessionRetryContext.create(getTableClient()).build();
    }

    private TableClient getTableClient() {
        CredentialProvider credentialProvider = OauthCredentialProvider.builder()
                .oauth(oauthToken)
                .build();
        AuthProvider authProvider = CloudAuthProvider.newAuthProvider(credentialProvider);

        GrpcTransport transport = GrpcTransport.forEndpoint(endpoint, database)
                .withAuthProvider(authProvider)
                .withSecureConnection()
                .build();

        return TableClient.newClient(GrpcTableRpc.ownTransport(transport))
                .build();
    }

    public String getDatabase() {
        return database;
    }
}
