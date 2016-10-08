package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadAction implements Action<CreateEntityDiscussionThreadResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private String comment;

    public CreateEntityDiscussionThreadAction(@Nonnull ProjectId projectId,
                                              @Nonnull OWLEntity entity,
                                              @Nonnull String comment) {
        this.entity = checkNotNull(entity);
        this.comment = checkNotNull(comment);
        this.projectId = checkNotNull(projectId);
    }

    @GwtSerializationConstructor
    public CreateEntityDiscussionThreadAction() {
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public String getComment() {
        return comment;
    }
}
