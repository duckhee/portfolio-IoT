package kr.co.won.study.api.assembler;

import kr.co.won.study.api.resource.dto.StudyListResourceDto;
import kr.co.won.study.domain.StudyDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageStudyAssembler implements RepresentationModelAssembler<StudyDomain, StudyListResourceDto> {

    private final ModelMapper modelMapper;

    @Override
    public StudyListResourceDto toModel(StudyDomain entity) {
        return null;
    }

    @Override
    public CollectionModel<StudyListResourceDto> toCollectionModel(Iterable<? extends StudyDomain> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
