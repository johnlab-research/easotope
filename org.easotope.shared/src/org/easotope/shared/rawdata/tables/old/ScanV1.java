/*
 * Copyright © 2016-2018 by Devon Bowen.
 *
 * This file is part of Easotope.
 *
 * Easotope is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * Additional permission under GNU GPL version 3 section 7:
 * If you modify this Program, or any covered work, by linking or combining
 * it with the Eclipse Rich Client Platform (or a modified version of that
 * library), containing parts covered by the terms of the Eclipse Public
 * License, the licensors of this Program grant you additional permission
 * to convey the resulting work. Corresponding Source for a non-source form
 * of such a combination shall include the source code for the parts of the
 * Eclipse Rich Client Platform used as well as that of the covered work.
 *
 * Easotope is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easotope. If not, see <http://www.gnu.org/licenses/>.
 */

package org.easotope.shared.rawdata.tables.old;

import org.easotope.framework.dbcore.DatabaseConstants;
import org.easotope.framework.dbcore.tables.TableObjectWithIntegerId;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName=ScanV1.TABLE_NAME)
public class ScanV1 extends TableObjectWithIntegerId {
	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "SCAN_V1";
	public static final String USERID_FIELD_NAME = "USERID";
	public static final String DATE_FIELD_NAME = "DATE";
	public static final String MASSSPECID_FIELD_NAME = "MASSSPECID";
	public static final String DISABLED_FIELD_NAME = "DISABLED";
	public static final String DESCRIPTION_FIELD_NAME = "DESCRIPTION";
	public static final String LEFT_BACKGROUND_X1 = "LEFTBACKGROUNDX1";
	public static final String LEFT_BACKGROUND_X2 = "LEFTBACKGROUNDX2";
	public static final String RIGHT_BACKGROUND_X1 = "RIGHTBACKGROUNDX1";
	public static final String RIGHT_BACKGROUND_X2 = "RIGHTBACKGROUNDX2";
	public static final String SLOPE = "SLOPE";
	public static final String INTERCEPT = "INTERCEPT";
	public static final String CHANNEL_TO_MZX10_FIELD_NAME = "CHANNELTOMZX10";

	@DatabaseField(columnName=USERID_FIELD_NAME, canBeNull=false)
	public int userId = DatabaseConstants.EMPTY_DB_ID;
	@DatabaseField(columnName=DATE_FIELD_NAME, canBeNull=false)
	public long date;
	@DatabaseField(columnName=MASSSPECID_FIELD_NAME, canBeNull=false)
	public int massSpecId = DatabaseConstants.EMPTY_DB_ID;
	@DatabaseField(columnName=DISABLED_FIELD_NAME, canBeNull=false)
	public boolean disabled;
	@DatabaseField(columnName=DESCRIPTION_FIELD_NAME, canBeNull=false)
	public String description;
	@DatabaseField(columnName=LEFT_BACKGROUND_X1, canBeNull=false, dataType=DataType.SERIALIZABLE)
	private Double[] leftBackgroundX1;
	@DatabaseField(columnName=LEFT_BACKGROUND_X2, canBeNull=false, dataType=DataType.SERIALIZABLE)
	private Double[] leftBackgroundX2;
	@DatabaseField(columnName=RIGHT_BACKGROUND_X1, canBeNull=false, dataType=DataType.SERIALIZABLE)
	private Double[] rightBackgroundX1;
	@DatabaseField(columnName=RIGHT_BACKGROUND_X2, canBeNull=false, dataType=DataType.SERIALIZABLE)
	private Double[] rightBackgroundX2;
	@DatabaseField(columnName=SLOPE, canBeNull=false, dataType=DataType.SERIALIZABLE)
	private Double[] slope;
	@DatabaseField(columnName=INTERCEPT, canBeNull=false, dataType=DataType.SERIALIZABLE)
	private Double[] intercept;
	@DatabaseField(columnName=CHANNEL_TO_MZX10_FIELD_NAME, dataType=DataType.SERIALIZABLE)
	public Integer[] channelToMzX10;

	public ScanV1() { }

	public ScanV1(ScanV1 scan) {
		super(scan);

		this.userId = scan.userId;
		this.date = scan.date;
		this.massSpecId = scan.massSpecId;
		this.disabled = scan.disabled;
		this.description = scan.description;
		this.leftBackgroundX1 = scan.leftBackgroundX1.clone();
		this.leftBackgroundX2 = scan.leftBackgroundX2.clone();
		this.rightBackgroundX1 = scan.rightBackgroundX1.clone();
		this.rightBackgroundX2 = scan.rightBackgroundX2.clone();
		this.slope = scan.slope.clone();
		this.intercept = scan.intercept.clone();
		
		if (scan.channelToMzX10 != null) {
			this.channelToMzX10 = new Integer[scan.channelToMzX10.length];

			for (int i=0; i<scan.channelToMzX10.length; i++) {
				this.channelToMzX10[i] = scan.channelToMzX10[i];
			}
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getMassSpecId() {
		return massSpecId;
	}

	public void setMassSpecId(int massSpecId) {
		this.massSpecId = massSpecId;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double[] getLeftBackgroundX1() {
		return leftBackgroundX1;
	}

	public void setLeftBackgroundX1(Double[] leftBackgroundX1) {
		this.leftBackgroundX1 = leftBackgroundX1;
	}

	public Double[] getLeftBackgroundX2() {
		return leftBackgroundX2;
	}

	public void setLeftBackgroundX2(Double[] leftBackgroundX2) {
		this.leftBackgroundX2 = leftBackgroundX2;
	}

	public Double[] getRightBackgroundX1() {
		return rightBackgroundX1;
	}

	public void setRightBackgroundX1(Double[] rightBackgroundX1) {
		this.rightBackgroundX1 = rightBackgroundX1;
	}

	public Double[] getRightBackgroundX2() {
		return rightBackgroundX2;
	}

	public void setRightBackgroundX2(Double[] rightBackgroundX2) {
		this.rightBackgroundX2 = rightBackgroundX2;
	}

	public Double[] getSlope() {
		return slope;
	}

	public void setSlope(Double[] slope) {
		this.slope = slope;
	}

	public Double[] getIntercept() {
		return intercept;
	}

	public void setIntercept(Double[] intercept) {
		this.intercept = intercept;
	}

	public Integer[] getChannelToMzX10() {
		return channelToMzX10;
	}

	public void setChannelToMzX10(Integer[] channelToMzX10) {
		this.channelToMzX10 = channelToMzX10;
	}
}
