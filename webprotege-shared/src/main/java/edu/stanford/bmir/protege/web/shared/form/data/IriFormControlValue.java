package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-07
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class IriFormControlValue implements PrimitiveFormControlData {

    public static IriFormControlValue get(@Nonnull IRI iri) {
        return new AutoValue_IriFormControlValue(iri);
    }

    @Nonnull
    public abstract IRI getIri();

    @Nonnull
    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<IRI> asIri() {
        return Optional.of(getIri());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }
}
