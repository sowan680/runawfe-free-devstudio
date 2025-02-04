package ru.runa.gpd.ui.wizard;

import org.eclipse.jface.wizard.Wizard;
import ru.runa.gpd.Localization;
import ru.runa.gpd.lang.model.ProcessDefinition;
import ru.runa.gpd.lang.model.Variable;
import ru.runa.gpd.lang.model.VariableContainer;

public class MakeLocalVariableWizard extends Wizard {
	 private VariableNamePage namePage;
	    private final VariableFormatPage formatPage;
	    private final VariableDefaultValuePage defaultValuePage;
	    private VariableAccessPage accessPage;
	    private final VariableStoreTypePage storeTypePage;
	    private Variable variable;
	
	public MakeLocalVariableWizard(ProcessDefinition processDefinition, Variable variable, boolean showNamePage, boolean editFormat) {
	    this(processDefinition, processDefinition, variable, showNamePage, editFormat, true);
	    }

	    public MakeLocalVariableWizard(ProcessDefinition processDefinition, VariableContainer variableContainer, Variable variable, boolean showNamePage,
	            boolean editFormat, boolean showAccessPage) {
	        if (showNamePage) {
	            namePage = new VariableNamePage(variableContainer, variable);
	        }
	        formatPage = new VariableFormatPage(processDefinition, variableContainer, variable, editFormat);
	        defaultValuePage = new VariableDefaultValuePage(variable);
	        if (showAccessPage) {
	            accessPage = new VariableAccessPage(variable);
	        }
	        namePage.setVariableName(variable.getName().substring(7));
	        namePage.setScriptingVariableName(variable.getScriptingName().substring(7));
	        storeTypePage = new VariableStoreTypePage(variable);
	        setWindowTitle(showNamePage ? Localization.getString("VariableWizard.create") : Localization.getString("VariableWizard.edit"));
	    }

	    @Override
	    public void addPages() {
	        if (namePage != null) {
	            addPage(namePage);
	        }
	        addPage(formatPage);
	        addPage(defaultValuePage);
	        if (accessPage != null) {
	            addPage(accessPage);
	        }
	        addPage(storeTypePage);
	    }

	    public Variable getVariable() {
	        return variable;
	    }
	
	 @Override
	    public boolean performFinish() {
	        String name = null;
	        String scriptingName = null;
	        String description = null;
	        if (namePage != null) {
	            name = namePage.getVariableName();
	            scriptingName = namePage.getScriptingVariableName();
	            description = namePage.getVariableDesc();
	        }
	        String formatClassName = formatPage.getType().getName();
	        String format = formatClassName;
	        if (formatPage.getComponentClassNames().length != 0) {
	            format += Variable.FORMAT_COMPONENT_TYPE_START;
	            for (int i = 0; i < formatPage.getComponentClassNames().length; i++) {
	                if (i != 0) {
	                    format += Variable.FORMAT_COMPONENT_TYPE_CONCAT;
	                }
	                format += formatPage.getComponentClassNames()[i];
	            }
	            format += Variable.FORMAT_COMPONENT_TYPE_END;
	        }
	        String defaultValue = defaultValuePage.getDefaultValue();
	        if (!defaultValuePage.isValidContent()) {
	            this.getContainer().showPage(defaultValuePage);
	            return false;
	        } else {
	            boolean publicVisibility = accessPage != null ? accessPage.isPublicVisibility() : false;
	            variable = new Variable(name, scriptingName, format, formatPage.getUserType());
	            variable.setPublicVisibility(publicVisibility);
	            variable.setDefaultValue(defaultValue == null || defaultValue.isEmpty() ? null : defaultValue);
	            variable.setDescription(description);
	            variable.setStoreType(storeTypePage.getStoreType());
	            return true;
	        }
	    }
}
