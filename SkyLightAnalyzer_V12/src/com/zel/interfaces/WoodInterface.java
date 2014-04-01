package com.zel.interfaces;

import com.zel.entity.Branch;
import com.zel.entity.enums.WordPojoType;

public interface WoodInterface {
	public int getPosition();

	public int getStatus();

	public void insertBranch(Branch branch);

	public Branch getBranch(int index);

	//得到base_index对应的位置上的值base_value
	public int getBase_value();
	//得到某词的数型,为数字、字母等
	public WordPojoType getWordType();
}
