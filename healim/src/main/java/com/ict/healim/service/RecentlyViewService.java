package com.ict.healim.service;

import java.util.List;

import com.ict.healim.vo.HospitalVO;
import com.ict.healim.vo.RecentlyViewVo;

public interface RecentlyViewService {

	public List<HospitalVO> getRecentlyViewList(String mber_id);
	
	
    // ���� �߰��� �ֱ� �� �ü� �߰� �޼���
    public int insertRecentlyView(String h_id, String mber_id);

}
