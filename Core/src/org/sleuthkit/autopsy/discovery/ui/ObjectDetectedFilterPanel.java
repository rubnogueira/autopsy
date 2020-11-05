/*
 * Autopsy
 *
 * Copyright 2020 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.discovery.ui;

import org.sleuthkit.autopsy.discovery.search.AbstractFilter;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.coreutils.ThreadConfined;
import org.sleuthkit.autopsy.discovery.search.SearchFiltering;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Class to allow configuration of the Objects Detected filter.
 */
final class ObjectDetectedFilterPanel extends AbstractDiscoveryFilterPanel {

    private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger(ObjectDetectedFilterPanel.class.getName());

    /**
     * Creates new form ObjectDetectedFilter.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    ObjectDetectedFilterPanel() {
        initComponents();
        setUpObjectFilter();
    }

    /**
     * Initialize the object filter.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    private void setUpObjectFilter() {
        int count = 0;
        try {
            DefaultListModel<String> objListModel = (DefaultListModel<String>) objectsList.getModel();
            objListModel.removeAllElements();
            List<String> setNames = DiscoveryUiUtils.getSetNames(BlackboardArtifact.ARTIFACT_TYPE.TSK_OBJECT_DETECTED, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DESCRIPTION);
            for (String name : setNames) {
                objListModel.add(count, name);
                count++;
            }
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error loading object detected set names", ex);
            objectsCheckbox.setEnabled(false);
            objectsList.setEnabled(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        objectsCheckbox = new javax.swing.JCheckBox();
        objectsScrollPane = new javax.swing.JScrollPane();
        objectsList = new javax.swing.JList<>();

        org.openide.awt.Mnemonics.setLocalizedText(objectsCheckbox, org.openide.util.NbBundle.getMessage(ObjectDetectedFilterPanel.class, "ObjectDetectedFilterPanel.text")); // NOI18N
        objectsCheckbox.setMaximumSize(new java.awt.Dimension(150, 25));
        objectsCheckbox.setMinimumSize(new java.awt.Dimension(150, 25));
        objectsCheckbox.setName(""); // NOI18N
        objectsCheckbox.setPreferredSize(new java.awt.Dimension(150, 25));
        objectsCheckbox.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        objectsCheckbox.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        objectsCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectsCheckboxActionPerformed(evt);
            }
        });

        setMinimumSize(new java.awt.Dimension(250, 30));
        setPreferredSize(new java.awt.Dimension(250, 30));

        objectsScrollPane.setName(""); // NOI18N
        objectsScrollPane.setPreferredSize(new java.awt.Dimension(27, 27));

        objectsList.setModel(new DefaultListModel<String>());
        objectsList.setEnabled(false);
        objectsList.setMaximumSize(new java.awt.Dimension(32767, 32767));
        objectsList.setVisibleRowCount(2);
        objectsScrollPane.setViewportView(objectsList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(objectsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(objectsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void objectsCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectsCheckboxActionPerformed
        objectsList.setEnabled(objectsCheckbox.isSelected());
    }//GEN-LAST:event_objectsCheckboxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox objectsCheckbox;
    private javax.swing.JList<String> objectsList;
    private javax.swing.JScrollPane objectsScrollPane;
    // End of variables declaration//GEN-END:variables

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    void configurePanel(boolean selected, int[] indicesSelected) {
        boolean hasObjects = objectsList.getModel().getSize() > 0;
        objectsCheckbox.setEnabled(hasObjects);
        objectsCheckbox.setSelected(selected && hasObjects);
        if (objectsCheckbox.isEnabled() && objectsCheckbox.isSelected()) {
            objectsScrollPane.setEnabled(true);
            objectsList.setEnabled(true);
            if (indicesSelected != null) {
                objectsList.setSelectedIndices(indicesSelected);
            }
        } else {
            objectsScrollPane.setEnabled(false);
            objectsList.setEnabled(false);
        }
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    JCheckBox getCheckbox() {
        return objectsCheckbox;
    }

    @Override
    JLabel getAdditionalLabel() {
        return null;
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @NbBundle.Messages({"ObjectDetectedFilterPanel.error.text=At least one object type name must be selected."})
    @Override
    String checkForError() {
        if (objectsCheckbox.isSelected() && objectsList.getSelectedValuesList().isEmpty()) {
            return Bundle.ObjectDetectedFilterPanel_error_text();
        }
        return "";
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    JList<?> getList() {
        return objectsList;
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    AbstractFilter getFilter() {
        if (objectsCheckbox.isSelected()) {
            return new SearchFiltering.ObjectDetectionFilter(objectsList.getSelectedValuesList());
        }
        return null;
    }

}
