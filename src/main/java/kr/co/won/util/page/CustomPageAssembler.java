package kr.co.won.util.page;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public class CustomPageAssembler<T> extends PageAssembler<T> {



    @Override
    public PagedModel<EntityModel<T>> toModel(Page<T> entity) {
        return super.toModel(entity);
    }

    @Override
    public CollectionModel<PagedModel<EntityModel<T>>> toCollectionModel(Iterable<? extends Page<T>> entities) {
        return super.toCollectionModel(entities);
    }
}
