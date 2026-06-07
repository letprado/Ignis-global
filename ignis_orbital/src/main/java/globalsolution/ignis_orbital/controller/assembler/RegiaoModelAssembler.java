package globalsolution.ignis_orbital.controller.assembler;

import globalsolution.ignis_orbital.controller.RegiaoController;
import globalsolution.ignis_orbital.dto.RegiaoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegiaoModelAssembler implements RepresentationModelAssembler<RegiaoResponse, EntityModel<RegiaoResponse>> {

    @Override
    public EntityModel<RegiaoResponse> toModel(RegiaoResponse regiao) {
        return EntityModel.of(regiao)
                .add(linkTo(methodOn(RegiaoController.class).buscarPorId(regiao.getId())).withSelfRel())
                .add(linkTo(methodOn(RegiaoController.class).listar()).withRel("regioes"))
                .add(linkTo(methodOn(RegiaoController.class).calcularIndice(regiao.getId())).withRel("indice-monitoramento"));
    }
}
