/*
 * Autopsy Forensic Browser
 *
 * Copyright 2018 Basis Technology Corp.
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

package org.sleuthkit.autopsy.contentviewers;

import java.awt.Component;
import java.util.List;
import org.sleuthkit.datamodel.AbstractFile;
import java.util.Arrays;
import com.dd.plist.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.ParserConfigurationException;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.TskCoreException;
import org.xml.sax.SAXException;


public class PListViewer extends javax.swing.JPanel implements FileTypeViewer, ExplorerManager.Provider {

    public static final String[] SUPPORTED_MIMETYPES = new String[]{"application/x-bplist"};
    private static final Logger LOGGER = Logger.getLogger(PListViewer.class.getName());
    
    private final org.openide.explorer.view.OutlineView outlineView;
    private final Outline outline;
    private ExplorerManager explorerManager;
    
    /**
     * Creates new form PListViewer
     */
    public PListViewer() {
       
        
        // Create an Outlineview and add to the panel
        outlineView = new org.openide.explorer.view.OutlineView();
        
        initComponents();
         
        //add(outlineView, BorderLayout.CENTER);
        
        outline = outlineView.getOutline();
        
        ((DefaultOutlineModel) outline.getOutlineModel()).setNodesColumnLabel("Key"); 
        
        Bundle.PListNode_KeyCol();
        outlineView.setPropertyColumns(
                //"Key", Bundle.PListNode_KeyCol(),
                "Type", Bundle.PListNode_TypeCol(),
                "Value", Bundle.PListNode_ValueCol());
         
        customize();
        
    }

    @NbBundle.Messages({"PListNode.KeyCol=Key",
    "PListNode.TypeCol=Type",
    "PListNode.ValueCol=Value" })
    
    
    private void customize() {
        
        //outlineView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        //outlineView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        outline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
        outline.setRootVisible(false);
        if (null == explorerManager) {
           explorerManager = new ExplorerManager();
        }
        
        plistTableScrollPane.setViewportView(outlineView);
        
        outline.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        
        this.setVisible(true);
        outline.setRowSelectionAllowed(false);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plistTableScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(plistTableScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public List<String> getSupportedMIMETypes() {
         return Arrays.asList(SUPPORTED_MIMETYPES);
    }

    @Override
    public void setFile(AbstractFile file) {
        processPlist(file);
    }

    @Override
    public Component getComponent() {
       return this;
    }

    @Override
    public void resetComponent() {
        // RAMAN TBD
       
    }

    
    /**
     * Process the given Plist file
     *
     * @param plistFile -
     *
     * @return none
     */
    private void processPlist(AbstractFile plistFile) {
       
        byte[] buf = new byte[(int) plistFile.getSize()];
        try {
            final int bytesRead = plistFile.read(buf, 0, plistFile.getSize());
        } catch (TskCoreException ex) {
            LOGGER.log(Level.SEVERE, "Error reading bytes of plist file.", ex);
        }

        
        List<PropKeyValue> plist = parsePList(buf);
        
         new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                setupTable(plist);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                setColumnWidths();
            }
        }.execute();
        
    }
    
    
     /**
     * Sets up the columns in the display table
     *
     * @param tableRows
     */
    void setupTable(List<PropKeyValue> tableRows) {

        explorerManager.setRootContext(new AbstractNode(Children.create(new PListRowFactory(tableRows), true)));
    }
    
    private void setColumnWidths() {
        int margin = 4;
        int padding = 8;

         // find the maximum width needed to fit the values for the first N rows, at most
        final int rows = Math.min(20, outline.getRowCount());
        for (int col = 0; col < outline.getColumnCount(); col++) {
            int columnWidthLimit = 2000;
            int columnWidth = 0;

            for (int row = 0; row < rows; row++) {
                TableCellRenderer renderer = outline.getCellRenderer(row, col);
                Component comp = outline.prepareRenderer(renderer, row, col);
          
                columnWidth = Math.max(comp.getPreferredSize().width, columnWidth);
            }

            columnWidth += 2 * margin + padding; // add margin and regular padding
            columnWidth = Math.min(columnWidth, columnWidthLimit);
            outline.getColumnModel().getColumn(col).setPreferredWidth(columnWidth);
        }
    }
    
   @NbBundle.Messages({"PListViewer.DataType.message=Binary Data value not shown"})
    private PropKeyValue parseProperty(String key, NSObject value) {
        if (value == null) {
            return null;
        } else if (value instanceof NSString) {
            return new PropKeyValue(key, PropertyType.STRING, value.toString());
        } else if (value instanceof NSNumber) {
            NSNumber number = (NSNumber) value;
            if (number.isInteger()) {
                return new PropKeyValue(key, PropertyType.NUMBER,  new Long(number.longValue()) );
            } else if (number.isBoolean()) {
                return new PropKeyValue(key, PropertyType.BOOLEAN,  new Boolean(number.boolValue()) );
            } else {
                return new PropKeyValue(key, PropertyType.NUMBER,  new Float(number.floatValue())) ;
            }
        } else if (value instanceof NSData ) {
            return new PropKeyValue(key, PropertyType.DATA, Bundle.PListViewer_DataType_message());
        } else if (value instanceof NSArray) {
            List<PropKeyValue> children = new ArrayList<>();
            NSArray array = (NSArray) value;
            
            PropKeyValue pkv = new PropKeyValue(key, PropertyType.ARRAY, array);
            for (int i = 0; i < array.count(); i++) {
                children.add(parseProperty("", array.objectAtIndex(i)));
            }
            
            pkv.setChildren(children.toArray(new PropKeyValue[0] ));
            return pkv;
        } else if (value instanceof NSDictionary) {
            List<PropKeyValue> children = new ArrayList<>();
            NSDictionary dict = (NSDictionary) value;
            
            PropKeyValue pkv = new PropKeyValue(key, PropertyType.DICTIONARY, dict);
            for (String key2 : ((NSDictionary) value).allKeys()) {
                NSObject o = ((NSDictionary) value).objectForKey(key2);
                children.add(parseProperty(key2, o));
            }
            
            pkv.setChildren(children.toArray(new PropKeyValue[0] ));
            return pkv;
        } else {
            LOGGER.severe("Can't parse Plist for key = " + key  + " value from " + value.getClass());
        }
        
        return null;
    }
    
    private List<PropKeyValue> parsePList(byte[] plistbytes) {

        List<PropKeyValue> plist = new ArrayList<>();

        try {
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(plistbytes);

            String[] keys = rootDict.allKeys();
            for (String key : keys) {
                PropKeyValue pkv = parseProperty(key, rootDict.objectForKey(key));
                if (null != pkv) { 
                    plist.add(pkv);
                }   
            }
        } catch (PropertyListFormatException | IOException | ParseException | ParserConfigurationException | SAXException ex) {
            LOGGER.log(Level.SEVERE, "Failed to parse PList.", ex);
            return null;
        }

        return plist;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        
        return explorerManager;
    }
    
    enum PropertyType {
            STRING, 
            NUMBER, 
            BOOLEAN, 
            DATA,
            ARRAY,
            DICTIONARY
    };
        
    class PropKeyValue {
   
        private final String key;
        private final PropertyType type;
        private final Object value;

        private PropKeyValue[] children = null;
        
        PropKeyValue(String key, PropertyType type, Object value) {
            this.key = key;
            this.type = type;
            this.value = value;
        }
        
        String getKey() {
            return this.key;
        }
        PropertyType getType() {
            return this.type;
        }
        
        Object getValue() {
            return this.value;
        }
        
        public PropKeyValue[] getChildren() {
            return children;
        }
        
        public void setChildren(PropKeyValue...children) {
            this.children = children;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane plistTableScrollPane;
    // End of variables declaration//GEN-END:variables
}
