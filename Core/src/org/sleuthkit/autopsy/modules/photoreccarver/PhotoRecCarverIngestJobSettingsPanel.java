/*
 * Autopsy Forensic Browser
 *
 * Copyright 2018-2020 Basis Technology Corp.
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
package org.sleuthkit.autopsy.modules.photoreccarver;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;
import org.sleuthkit.autopsy.coreutils.Logger;

/**
 * Ingest job settings panel for the Encryption Detection module.
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
final class PhotoRecCarverIngestJobSettingsPanel extends IngestModuleIngestJobSettingsPanel {

    private static final Logger logger = Logger.getLogger(PhotoRecCarverIngestJobSettingsPanel.class.getName());
    private static final String EXTENSION_LIST_SEPARATOR = ",";
    private static final String PHOTOREC_TYPES_URL = "https://www.cgsecurity.org/wiki/File_Formats_Recovered_By_PhotoRec";

    /**
     * Instantiate the ingest job settings panel.
     *
     * @param settings The ingest job settings.
     */
    public PhotoRecCarverIngestJobSettingsPanel(PhotoRecCarverIngestJobSettings settings) {
        initComponents();
        customizeComponents(settings);
    }

    /**
     * Update components with values from the ingest job settings.
     *
     * @param settings The ingest job settings.
     */
    private void customizeComponents(PhotoRecCarverIngestJobSettings settings) {
        includeExcludeCheckbox.setSelected(settings.hasFileOptOption());
        extensionListTextfield.setText(String.join(EXTENSION_LIST_SEPARATOR, settings.getIncludeExcludeExtensions()));
        includeRadioButton.setSelected(!settings.isIncludeElseExclude());
        excludeRadioButton.setSelected(!settings.isIncludeElseExclude());
        keepCorruptedFilesCheckbox.setSelected(settings.isKeepCorruptedFiles());
        setupTypesHyperlink();
        setIncludePanelEnabled();
    }

    /**
     * Sets up a clickable hyperlink for the different supported types for
     * extensions.
     */
    private void setupTypesHyperlink() {
        // taken from https://www.codejava.net/java-se/swing/how-to-create-hyperlink-with-jlabel-in-java-swing
        this.fullListOfTypesHyperlink.setForeground(Color.BLUE.darker());
        this.fullListOfTypesHyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.fullListOfTypesHyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(PHOTOREC_TYPES_URL));
                } catch (IOException | URISyntaxException ex) {
                    logger.log(Level.WARNING, "There was an error going to types huperlink: " + PHOTOREC_TYPES_URL, ex);
                }
            }
        });

    }

    /**
     * Whether or not the file type inclusion/exclusion panel should be enabled
     * based on whether or not the includeExcludeCheckbox is checked.
     */
    private void setIncludePanelEnabled() {
        setIncludePanelEnabled(includeExcludeCheckbox.isSelected());
    }

    /**
     * Sets components in the inclusion/exclusion panel to the specified enabled
     * state.
     *
     * @param enabled Whether or not to enable components.
     */
    private void setIncludePanelEnabled(boolean enabled) {
        includeRadioButton.setEnabled(enabled);
        excludeRadioButton.setEnabled(enabled);
        extensionListLabel.setEnabled(enabled);
        extensionListTextfield.setEnabled(enabled);
        exampleLabel.setEnabled(enabled);
        fullListOfTypesLabel.setEnabled(enabled);
    }

    @Override
    public IngestModuleIngestJobSettings getSettings() {
        return new PhotoRecCarverIngestJobSettings(
                keepCorruptedFilesCheckbox.isSelected(),
                includeExcludeCheckbox.isSelected(),
                includeRadioButton.isSelected(),
                getExtensions(extensionListTextfield.getText()));
    }

    /**
     * Determines a list of extensions to pass as parameters to photorec based
     * on the specified input. Splits on separator and trims.
     *
     * @param combinedList The comma-separated list.
     *
     * @return The list of strings to use with photorec.
     */
    private List<String> getExtensions(String combinedList) {
        if (StringUtils.isBlank(combinedList)) {
            return Collections.emptyList();
        }

        return Stream.of(combinedList.split(EXTENSION_LIST_SEPARATOR))
                .map(ext -> ext.trim())
                .filter(ext -> StringUtils.isNotBlank(ext))
                .collect(Collectors.toList());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        includeExcludeButtonGroup = new javax.swing.ButtonGroup();
        keepCorruptedFilesCheckbox = new javax.swing.JCheckBox();
        javax.swing.JLabel detectionSettingsLabel = new javax.swing.JLabel();
        includeExcludeCheckbox = new javax.swing.JCheckBox();
        excludeRadioButton = new javax.swing.JRadioButton();
        exampleLabel = new javax.swing.JLabel();
        fullListOfTypesLabel = new javax.swing.JLabel();
        extensionListLabel = new javax.swing.JLabel();
        fullListOfTypesHyperlink = new javax.swing.JLabel();
        extensionListTextfield = new javax.swing.JTextField();
        includeRadioButton = new javax.swing.JRadioButton();

        org.openide.awt.Mnemonics.setLocalizedText(keepCorruptedFilesCheckbox, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.keepCorruptedFilesCheckbox.text")); // NOI18N

        detectionSettingsLabel.setFont(detectionSettingsLabel.getFont().deriveFont(detectionSettingsLabel.getFont().getStyle() | java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(detectionSettingsLabel, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.detectionSettingsLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(includeExcludeCheckbox, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.includeExcludeCheckbox.text")); // NOI18N
        includeExcludeCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeExcludeCheckboxActionPerformed(evt);
            }
        });

        includeExcludeButtonGroup.add(excludeRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(excludeRadioButton, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.excludeRadioButton.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(exampleLabel, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.exampleLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(fullListOfTypesLabel, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.fullListOfTypesLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(extensionListLabel, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.extensionListLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(fullListOfTypesHyperlink, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.fullListOfTypesHyperlink.text")); // NOI18N

        extensionListTextfield.setText(org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.extensionListTextfield.text")); // NOI18N

        includeExcludeButtonGroup.add(includeRadioButton);
        includeRadioButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(includeRadioButton, org.openide.util.NbBundle.getMessage(PhotoRecCarverIngestJobSettingsPanel.class, "PhotoRecCarverIngestJobSettingsPanel.includeRadioButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(detectionSettingsLabel)
                            .addComponent(keepCorruptedFilesCheckbox)
                            .addComponent(includeExcludeCheckbox)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(includeRadioButton)
                            .addComponent(excludeRadioButton)
                            .addComponent(exampleLabel)
                            .addComponent(extensionListTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fullListOfTypesLabel)
                            .addComponent(fullListOfTypesHyperlink, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(extensionListLabel))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(detectionSettingsLabel)
                .addGap(0, 2, 2)
                .addComponent(keepCorruptedFilesCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includeExcludeCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludeRadioButton)
                .addGap(4, 4, 4)
                .addComponent(extensionListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(extensionListTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exampleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullListOfTypesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullListOfTypesHyperlink)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void includeExcludeCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includeExcludeCheckboxActionPerformed
        setIncludePanelEnabled();
    }//GEN-LAST:event_includeExcludeCheckboxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel exampleLabel;
    private javax.swing.JRadioButton excludeRadioButton;
    private javax.swing.JLabel extensionListLabel;
    private javax.swing.JTextField extensionListTextfield;
    private javax.swing.JLabel fullListOfTypesHyperlink;
    private javax.swing.JLabel fullListOfTypesLabel;
    private javax.swing.ButtonGroup includeExcludeButtonGroup;
    private javax.swing.JCheckBox includeExcludeCheckbox;
    private javax.swing.JRadioButton includeRadioButton;
    private javax.swing.JCheckBox keepCorruptedFilesCheckbox;
    // End of variables declaration//GEN-END:variables
}
