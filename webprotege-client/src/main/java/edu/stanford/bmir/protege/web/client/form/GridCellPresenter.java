package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Optionality.REQUIRED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridCellPresenter implements HasRequestFocus {

    @Nonnull
    private final GridCellView view;

    @Nonnull
    private Optional<GridColumnDescriptor> descriptor = Optional.empty();

    @Nonnull
    private final FormControlFactory formControlFactory;

    private FormFieldControl editor;

    @Inject
    public GridCellPresenter(@Nonnull GridCellView view,
                             @Nonnull FormControlFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.formControlFactory = formControlFactory;
    }

    public void clear() {
        editor.clearValue();
        updateValueRequired();
    }

    public Optional<GridColumnId> getId() {
        return descriptor.map(GridColumnDescriptor::getId);
    }

    public void requestFocus() {
        view.requestFocus();
    }

    public void setDescriptor(GridColumnDescriptor column) {
        FormControlDescriptor formControlDescriptor = column.getFormControlDescriptor();
        ValueEditorFactory<FormControlData> valueEditorFactory = formControlFactory.getValueEditorFactory(formControlDescriptor);
        editor = new FormFieldControlImpl(valueEditorFactory, column.getRepeatability(), true);
        view.getEditorContainer().setWidget(editor);
        this.descriptor = Optional.of(column);
    }

    public void start(AcceptsOneWidget cellContainer) {
        cellContainer.setWidget(view);
    }

    public void setValue(GridCellData data) {
        editor.clearValue();
        editor.setValue(data.getValues());
        updateValueRequired();
    }

    public GridCellData getValue() {
        if(!descriptor.isPresent()) {
            return GridCellData.get(GridColumnId.get("null"), ImmutableList.of());
        }
        GridColumnDescriptor columnDescriptor = descriptor.get();
        return editor.getValue().map(v -> GridCellData.get(columnDescriptor.getId(),
                                                           ImmutableList.copyOf(v)))
              .orElse(GridCellData.get(columnDescriptor.getId(), ImmutableList.of()));
    }

    public boolean isPresent() {
        return getId().isPresent();
    }

    private void updateValueRequired() {
        descriptor.ifPresent(descriptor -> {
            if (descriptor.getOptionality() == REQUIRED) {
                boolean requiredValueNotPresent = !editor.getValue().isPresent();
                view.setRequiredValueNotPresentVisible(requiredValueNotPresent);
            }
            else {
                view.setRequiredValueNotPresentVisible(false);
            }
        });
    }

}
