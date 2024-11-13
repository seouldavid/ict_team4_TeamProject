package com.ict.healim.service;

import java.util.List;

import com.ict.healim.vo.HospitalVO;
import com.ict.healim.vo.JoinVO;

public interface JoinService {
	//ȸ������
	public int join(JoinVO jvo);
	
	// �̸��� �ߺ����� Ȯ��
	public boolean isEmailAvailable(String mberId);
	
	// ���������� ��������
	public JoinVO getMyPage(String mberid);
	
	// ���������� ����
	public int MyPageUpdate(JoinVO jvo);
	
	// ȸ������ �α� ����
	public int joinLog(JoinVO jvo);
	
	// ��纴�� �� ��� ��������
	public List<HospitalVO> getHospitalPick(String mber_id);
	
	// ���� �� ��� ��������
	public List<HospitalVO> getNHPick(String mber_id);
	
	// �� ��� ����
	public int removePick(String mber_id, String h_id);
	
	// ��纴�� �� ���� ��������
	public int getHPickCount(String mber_id);
	
	// ���� �� ���� ��������
	public int getNPickCount(String mber_id);
}
