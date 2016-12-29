package com.siegler.Client.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.siegler.Client.utils.ClientConstants;

public class JListWithXButton extends JPanel {

	private static final long serialVersionUID = -1677963373907423466L;

	private static final String FONT = ClientConstants.DEFAULT_FONT;

	private static final int LABELS_SIZE = 14;

	private static final int LABEL_HEIGHT = 50;

	private static final int X_BUTTON_WIDTH = 60;

	private JScrollPane scroll;

	private JTable table;
	
	private JTextField searchField;
		
	public JListWithXButton(List<String> labels) {

		super();
		
		searchField = new JTextField();
		searchField.setForeground(new Color(0.1f, 0.1f, 0.1f, 0.5f));
		searchField.setText("Search");
		searchField.setFont(new Font(FONT, Font.PLAIN, LABELS_SIZE));
		searchField.setHorizontalAlignment(JTextField.CENTER);
				
		searchField.addFocusListener(new FocusListener(){
			
			@Override
			public void focusGained(FocusEvent e) {

				searchField.setForeground(new Color(0f, 0f, 0f, 1f));
				searchField.setText("");
				searchField.setPreferredSize(new Dimension(getWidth(), searchField.getHeight()));
				searchField.setHorizontalAlignment(JTextField.LEFT);
				updateUI();
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				searchField.setForeground(new Color(0.1f, 0.1f, 0.1f, 0.5f));
				searchField.setText("Search");
				searchField.setHorizontalAlignment(JTextField.CENTER);
				updateUI();
				
			}
			
		});
		
		table = new JTable(new JTableModel(labels.toArray(new String[labels.size()]), labels.size()));
		table.setFont(new Font(FONT, Font.BOLD, LABELS_SIZE));

		table.setFillsViewportHeight(true);
		table.setRowHeight(LABEL_HEIGHT);
		table.getColumn("X Button").setMaxWidth(X_BUTTON_WIDTH);
		table.getColumn("X Button").setMinWidth(X_BUTTON_WIDTH);
		table.setShowVerticalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultRenderer(String.class, new VisitorRenderer());
		createKeybindings(table);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);

		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		table.getColumn("X Button").setCellRenderer(buttonRenderer);
		table.addMouseListener(new JTableButtonMouseListener(table));
		
		table.setTableHeader(null);

		scroll = new JScrollPane(table);

		this.add(searchField, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.setFocusTraversalKeysEnabled(false);

	}
	
	@Override
	public void setPreferredSize(Dimension d){
		
		super.setPreferredSize(d);

		this.table.setPreferredScrollableViewportSize(d);
		this.table.setRowHeight(LABEL_HEIGHT);
		this.table.setPreferredSize(d);
		this.searchField.setPreferredSize(new Dimension((int) d.getWidth(), (int) this.searchField.getPreferredSize().getHeight())); // CHANGE THIS SO IT IS RIGHT
		
	}

	private void createKeybindings(JTable table) {
		
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		
		table.getActionMap().put("Enter", new AbstractAction() {

			private static final long serialVersionUID = -581467806445161692L;

			@Override
			public void actionPerformed(ActionEvent e) {}
			
		});
		
	}

	public static class JTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -456932211L;

		private static final String[] COLUMN_NAMES = new String[] {"Chat Name", "X Button"};

		private static final Class<?>[] COLUMN_TYPES = new Class<?>[] {String.class, JButton.class};

		private int numRows;

		private String[] labels;

		public JTableModel(String[] labels, int numRows) {

			this.numRows = numRows;
			this.labels = labels;

		}

		public JTableModel(String labels[]) {

			this(labels, 4);

		}

		@Override
		public int getColumnCount() {

			return COLUMN_NAMES.length;

		}

		@Override
		public int getRowCount() {

			return numRows;

		}

		@Override
		public String getColumnName(int columnIndex) {

			return COLUMN_NAMES[columnIndex];

		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {

			return COLUMN_TYPES[columnIndex];

		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {

			switch (columnIndex) {

			case 0:
				return labels[rowIndex];

			case 1:
				final JButton button = new JButton("x");

				button.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(button),
								"Button clicked for row " + rowIndex);

					}

				});

				return button;

			default:
				return "Unknown request or index out of bounds";

			}

		}

	}

	private static class JTableButtonRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			JButton button = (JButton) value;

			button.setBorderPainted(false);
			button.setForeground(new Color(1f, 1f, 1f));

			if (isSelected) {

				button.setOpaque(true);
				button.setContentAreaFilled(true);
				button.setBorderPainted(false);
				button.setBackground(table.getSelectionBackground());

			}

			else {

				button.setOpaque(false);
				button.setContentAreaFilled(false);

			}

			return button;

		}

	}

	private static class JTableButtonMouseListener extends MouseAdapter {

		private static final int HORIZONTAL_BUTTON_PRESSING_TOLERANCE = (int) (new Font(FONT, Font.PLAIN, LABELS_SIZE)
				.getStringBounds("x", new FontRenderContext(new AffineTransform(), true, true)).getWidth());
		private static final int VERTICAL_BUTTON_PRESSING_TOLERANCE = (int) (new Font(FONT, Font.PLAIN, LABELS_SIZE)
				.getStringBounds("x", new FontRenderContext(new AffineTransform(), true, true)).getHeight()) - 12;

		private final JTable table;

		public JTableButtonMouseListener(JTable table) {

			this.table = table;

		}

		public void mouseClicked(MouseEvent e) {

			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row = e.getY() / table.getRowHeight();

			int width = table.getWidth();
			int x = e.getX();
			int y = e.getY();

			if (x >= width - (X_BUTTON_WIDTH / 2) - HORIZONTAL_BUTTON_PRESSING_TOLERANCE
					&& x <= width - (X_BUTTON_WIDTH / 2) + HORIZONTAL_BUTTON_PRESSING_TOLERANCE) {

				int relativeY = y % LABEL_HEIGHT;

				if (relativeY >= (LABEL_HEIGHT / 2) - VERTICAL_BUTTON_PRESSING_TOLERANCE
						&& relativeY <= (LABEL_HEIGHT / 2) + VERTICAL_BUTTON_PRESSING_TOLERANCE) {

					if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {

						Object value = table.getValueAt(row, column);

						if (value instanceof JButton) {

							((JButton) value).doClick();

						}

					}

				}

			}

		}

	}

	private static class VisitorRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 2495789779713594638L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setBorder(noFocusBorder);

			return this;

		}

	}

}