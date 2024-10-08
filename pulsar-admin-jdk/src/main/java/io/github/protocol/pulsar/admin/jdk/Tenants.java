package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.protocol.pulsar.admin.common.JacksonService;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class Tenants {

    private final InnerHttpClient httpClient;

    public Tenants(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void createTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.put(
                            String.format("%s/%s", UrlConst.TENANTS, tenant), tenantInfo);
            if (response.statusCode() != 204){
                throw new PulsarAdminException(String.format("failed to create tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void deleteTenant(String tenant, boolean force) throws PulsarAdminException {
        try {
            HttpResponse<String> response =
                    httpClient.delete(String.format("%s/%s", UrlConst.TENANTS, tenant), "force", String.valueOf(force));
            if (response.statusCode() != 204){
                throw new PulsarAdminException(String.format("failed to create tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void updateTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException {
        try {
            HttpResponse<String> response =
                    httpClient.post(
                            String.format("%s/%s", UrlConst.TENANTS, tenant), tenantInfo);
            if (response.statusCode() != 204){
                throw new PulsarAdminException(String.format("failed to update tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    public TenantInfo getTenantAdmin(String tenant) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                            String.format("%s/%s", UrlConst.TENANTS, tenant));
            if (response.statusCode() != 200){
                throw new PulsarAdminException(String.format("failed to get tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
            return JacksonService.toObject(response.body(), TenantInfo.class);
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    public List<String> getTenants() throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(UrlConst.TENANTS);
            if (response.statusCode() != 200){
                throw new PulsarAdminException(String.format("failed to get list of tenant, status code %s, body : %s",
                        response.statusCode(), response.body()));
            }
            return JacksonService.toList(response.body(), new TypeReference<List<String>>(){});
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }
}
