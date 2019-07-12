package com.concept.tenant;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import com.concept.util.TenantContextHolder;

/**O Hibernate precisa saber qual banco de dados usar, ou seja, qual locatário se conectar
 * para. Esta classe fornece um mecanismo para fornecer a fonte de dados correta na execução
 * Tempo.
*/
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		TenantContextHolder.setTenantId(TenancyInterceptor.getEmpresa());
		String tenant = TenantContextHolder.getTenant();
		return StringUtils.isNotBlank(tenant) ? tenant : "concept";
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
