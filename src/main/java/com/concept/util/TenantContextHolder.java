package com.concept.util;
/**
 * Quando o usuário final envia o formulário de login, o ID do locatário é necessário para
 * determinar qual banco de dados para se conectar. Isso precisa ser capturado no
 * Primavera mecanismo de autenticação de segurança, especificamente no
 * {@link UsernamePasswordAuthenticationFilter} implementado por
 * {@link CustomAuthenticationFilter}. Este ID do inquilino é então exigido pelo
 * {@link CurrentTenantIdentifierResolver} implementado pelo
 * {@link CurrentTenantIdentifierResolverImpl}
 *
 * <br/>
 * <br/>
 * <b> Explicação: </ b> Thread Local pode ser considerado como um escopo de acesso, como
 * um escopo de solicitação ou escopo de sessão. É um escopo de thread. Você pode definir qualquer objeto
 * em Thread Local e este objeto será global e local para o específico
 * thread que está acessando este objeto. Global e local ao mesmo tempo? :
 *
 * <ul>
 * <li> Os valores armazenados no Thread Local são globais para o encadeamento, o que significa que eles
 * pode ser acessado de qualquer lugar dentro desse segmento. Se um encadeamento chama métodos
 * de várias classes, então todos os métodos podem ver a variável Thread Local
 * definido por outros métodos (porque eles estão executando no mesmo segmento). O valor que
 * não precisa ser passado explicitamente. É como você usa variáveis ​​globais. </ Li>
 * <li> Os valores armazenados no Thread Local são locais para o encadeamento, o que significa que cada
 * o segmento terá sua própria variável de segmento local. Um segmento não pode
 * acessar / modificar as variáveis ​​locais de thread de outros segmentos. </ li>
 * </ ul>
*/
public class TenantContextHolder {
	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setTenantId(String tenant) {
        CONTEXT.set(tenant);
    }

    public static String getTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
