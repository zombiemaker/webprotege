package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Optionality.REQUIRED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public class FormFieldPresenter {

    private void handleFormControlValueChanged(ValueChangeEvent<Optional<List<FormControlData>>> event) {
        updateRequiredValuePresent();
    }

    enum ExpansionState {
        EXPANDED,
        COLLAPSED
    }

    @Nonnull
    private final FormFieldView view;

    @Nonnull
    private final FormFieldDescriptor formFieldDescriptor;

    @Nonnull
    private final FormControlFactory formControlFactory;

    private FormFieldControl formControl;

    private ExpansionState expansionState = ExpansionState.EXPANDED;

    public FormFieldPresenter(@Nonnull FormFieldView view,
                              @Nonnull FormFieldDescriptor formFieldDescriptor,
                              @Nonnull FormControlFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.formFieldDescriptor = checkNotNull(formFieldDescriptor);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    @Nonnull
    public FormFieldView getFormFieldView() {
        return view;
    }

    public boolean isDirty() {
        return formControl.isDirty();
    }

    protected FormFieldView start() {
        FormControlDescriptor formControlDescriptor = formFieldDescriptor.getFormControlDescriptor();
        formControlFactory.getValueEditorFactory(formControlDescriptor);

        ValueEditorFactory<FormControlData> valueEditorFactory = formControlFactory.getValueEditorFactory(
                formControlDescriptor);

        formControl = new FormFieldControlImpl(valueEditorFactory, formFieldDescriptor.getRepeatability(), false);
        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        String langTag = localeInfo.getLocaleName();
        view.setId(formFieldDescriptor.getId());
        view.setFormLabel(formFieldDescriptor.getLabel().get(langTag));
        view.setEditor(formControl);
        view.setRequired(formFieldDescriptor.getOptionality());
        view.setHelpText(formFieldDescriptor.getHelp().get(langTag));
        Map<String, String> style = formFieldDescriptor.getStyle();
        style.forEach(view::addStylePropertyValue);
        // Update the required value missing display when the value changes
        formControl.addValueChangeHandler(this::handleFormControlValueChanged);
        view.setHeaderClickedHandler(this::toggleExpansionState);
        updateRequiredValuePresent();
        return view;
    }

    @Nonnull
    public ExpansionState getExpansionState() {
        return expansionState;
    }

    public void toggleExpansionState() {
        if(expansionState == ExpansionState.EXPANDED) {
            setExpansionState(ExpansionState.COLLAPSED);
        }
        else {
            setExpansionState(ExpansionState.EXPANDED);
        }
    }

    public void setExpansionState(ExpansionState expansionState) {
        this.expansionState = expansionState;
        if(expansionState == ExpansionState.EXPANDED) {
            view.expand();
        }
        else {
            view.collapse();
        }

    }

    public FormFieldData getValue() {
        if(formControl == null) {
            return FormFieldData.get(formFieldDescriptor, ImmutableList.of(), 0);
        }
        ImmutableList<FormControlData> formControlData = formControl.getEditorValue()
                                                                    .map(ImmutableList::copyOf)
                                                                    .orElse(ImmutableList.of());

        return FormFieldData.get(formFieldDescriptor, formControlData, formControlData.size());
    }

    public void setValue(@Nonnull FormFieldData formFieldData) {
        if(formControl == null) {
            return;
        }
        if(!formFieldData.getFormFieldDescriptor().equals(formFieldDescriptor)) {
            throw new RuntimeException("FormFieldDescriptor mismatch for field: " + formFieldDescriptor.getId());
        }
        checkNotNull(formFieldData);
        ImmutableList<FormControlData> formControlData = formFieldData.getFormControlData();
        formControl.setValue(formControlData);
        int formControlDataCount = formFieldData.getFormControlDataCount();
        if(formControlDataCount > formControlData.size()) {
            view.setLimitedValuesDisplayed(formControlData.size(), formControlDataCount);
        }
        updateRequiredValuePresent();
    }

    public void clearValue() {
        if(formControl == null) {
            return;
        }
        formControl.clearValue();
        updateRequiredValuePresent();
    }

    /**
     * Updates the specified view so that there is a visual indication if the value is required but not present.
     */
    private void updateRequiredValuePresent() {
        if(formControl == null) {
            view.setRequiredValueNotPresentVisible(false);
            return;
        }
        GWT.log("[FormFieldPresenter] updating required " + formFieldDescriptor.getId());
        if (formFieldDescriptor.getOptionality() == REQUIRED) {
            boolean requiredValueNotPresent = !formControl.getValue().isPresent();
            GWT.log("[FormFieldPresenter] Required value not present: " + requiredValueNotPresent);
            view.setRequiredValueNotPresentVisible(requiredValueNotPresent);
        }
        else {
            GWT.log("[FormFieldPresenter] NOT REQUIRED");
            view.setRequiredValueNotPresentVisible(false);
        }
    }
}
