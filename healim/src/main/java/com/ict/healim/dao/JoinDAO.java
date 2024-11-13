package com.ict.healim.dao;

import java.util.List;

import com.ict.healim.vo.HospitalVO;
import com.ict.healim.vo.JoinVO;

public interface JoinDAO {
	// ȸ������
	public int join(JoinVO jvo);
	// ȸ������ �̸��� �������� Ȯ��
	public int countByEmail(String mberId);
	
	// ���������� ��������
	public JoinVO getMyPage(String mberid);
	
	// ���������� ����
	public int MyPageUpdate(JoinVO jvo);
	
	// ȸ������ �α� ����
	public int joinLog(JoinVO jvo);
	
	// ��� ���� �� ��� ��������
	public List<HospitalVO> getHospitalPick(String mber_id);
	
	// ���� �� ��� ��������
	public List<HospitalVO> getNHPick(String mber_id);
	
	// �� ��� ����
	public int removePick(String mber_id, String h_id);
	
	// ��� ���� �� ���� ��������
	public int getHPickCount(String mber_id);
	
	// ���� �� ���� ��������
	public int getNPickCount(String mber_id);
}
