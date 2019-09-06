package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
public class OntologyAxiomsIndexImpl implements OntologyAxiomsIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public OntologyAxiomsIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> getAxioms(@Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        return ontologyIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ont -> ont.getAxioms().stream());
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(axiom);
        checkNotNull(ontologyId);
        return ontologyIndex.getOntology(ontologyId)
                            .map(ont -> ont.containsAxiom(axiom))
                            .orElse(false);
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                                  @Nonnull OWLOntologyID ontologyId) {
        return containsAxiom(axiom, ontologyId)
                ||
                ontologyIndex.getOntology(ontologyId)
                             .map(ont -> ont.containsAxiomIgnoreAnnotations(axiom))
                             .orElse(false);
    }
}
