package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public interface PrimitiveFormControlData {

    static PrimitiveFormControlData get(OWLEntity entity) {
        return EntityFormControlData.get(entity);
    }

    static PrimitiveFormControlData get(IRI iri) {
        return IriFormControlValue.get(iri);
    }

    static PrimitiveFormControlData get(OWLLiteral literal) {
        return LiteralFormControlValue.get(literal);
    }

    static PrimitiveFormControlData get(String text) {
        return LiteralFormControlValue.get(DataFactory.getOWLLiteral(text));
    }

    static PrimitiveFormControlData get(double value) {
        return LiteralFormControlValue.get(DataFactory.getOWLLiteral(value));
    }

    static PrimitiveFormControlData get(boolean value) {
        return LiteralFormControlValue.get(DataFactory.getOWLLiteral(value));
    }

    @Nonnull
    Optional<OWLEntity> asEntity();

    @Nonnull
    Optional<IRI> asIri();

    @Nonnull
    Optional<OWLLiteral> asLiteral();
}
