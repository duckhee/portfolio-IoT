package kr.co.won.util.page;

import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.util.UriComponents;

public class PagingResourcesAssembler<T> extends PagedResourcesAssembler<T> {
    /**
     * Creates a new {@link PagedResourcesAssembler} using the given {@link PageableHandlerMethodArgumentResolver} and
     * base URI. If the former is {@literal null}, a default one will be created. If the latter is {@literal null}, calls
     * to {@link #toModel(Page)} will use the current request's URI to build the relevant previous and next links.
     *
     * @param resolver can be {@literal null}.
     * @param baseUri  can be {@literal null}.
     */
    public PagingResourcesAssembler(HateoasPageableHandlerMethodArgumentResolver resolver, UriComponents baseUri) {
        super(resolver, baseUri);
    }
}
