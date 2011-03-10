package com.googlecode.simpleprofiler.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.googlecode.simpleprofiler.model.Methodlog;
import com.googlecode.simpleprofiler.table.model.MethodAnalyzeModel;
import com.verigy.itee.ui.common.widgets.vtable.VTable;
import com.verigy.itee.ui.common.widgets.vtable.VTableSortOnClick;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.KTable.KTX;

public class SummaryView extends ViewPart {

	private KTable table;

	public SummaryView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		table = new VTable(parent, SWT.V_SCROLL | SWT.H_SCROLL
				| SWTX.EDIT_ON_KEY | SWTX.RECT_SELECTION | SWTX.MULTI,
				KTX.HEADER_ROW_SELECTION | KTX.HEADER_COLUMN_SELECTION
						| KTX.SELECT_COLUMN_ON_RIGHT_CLICK
						| KTX.EXPAND_CELL_SELECTION | KTX.EXPAND_ROW_SELECTION);
		Methodlog[] methodLogs = getMockData();
 		table.setModel(new MethodAnalyzeModel(methodLogs));
		table.addCellSelectionListener(new VTableSortOnClick(table));

	}

	// (String fullName, int allTime, int execNum)
	private Methodlog[] getMockData() {
		Methodlog[] m = new Methodlog[10000];
		for (int i = 0; i < m.length; i++) {
			m[i] = new Methodlog("methodxslfasflk" + i, (i + 90) % 100, i % 100);
		}
		return m;

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
