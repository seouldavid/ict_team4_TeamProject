package com.ict.healim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.healim.dao.JoinDAO;
import com.ict.healim.vo.HospitalVO;
import com.ict.healim.vo.JoinVO;

@Service
public class JoinServiceImpl implements JoinService{
	
	@Autowired
	private JoinDAO joinDAO;
	
	
	// ȸ������
	@Override
	public int join(JoinVO jvo) {
		// ������ �����ϰ� �����ͺ��̽��� �Է�
		String dbPassword = jvo.getMbtlnum();
		dbPassword = dbPassword.replaceAll("-", "");
		jvo.setMbtlnum(dbPassword);
		return joinDAO.join(jvo);
	}
	// �̸��� �ߺ�����
	@Override
	public boolean isEmailAvailable(String mberId) {
		// TODO Auto-generated method stub
		return joinDAO.countByEmail(mberId) == 0;
	}
	
	// �ڵ��� ��ȣ�� �������� �߰��ϴ� �޼���
    private String formatMbtlnum(String phoneNumber) {
        if (phoneNumber.length() == 10) {
            return phoneNumber.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        } else if (phoneNumber.length() == 11) {
            return phoneNumber.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }
        return phoneNumber;
    }
    // ȸ������ �α� ����
    @Override
    public int joinLog(JoinVO jvo) {
    	return joinDAO.joinLog(jvo);
    }
	
    // ���������� ��������
	@Override
	public JoinVO getMyPage(String mberid) {
		JoinVO jvo = joinDAO.getMyPage(mberid);
		// �ڵ��� ��ȣ�� �������� �߰��Ͽ� ����
        String mbtlnum = formatMbtlnum(jvo.getMbtlnum());
        jvo.setMbtlnum(mbtlnum);
        return jvo;
	}
	
	// ���������� ������� ����
	@Override
	public int MyPageUpdate(JoinVO jvo) {
		// ������ �����ϰ� �����ͺ��̽��� �Է�
		String dbPassword = jvo.getMbtlnum();
		dbPassword = dbPassword.replaceAll("-", "");
		jvo.setMbtlnum(dbPassword);
		return joinDAO.MyPageUpdate(jvo);
	}
	
	// ���������� ��纴�� �� ��� ��������
	@Override
	public List<HospitalVO> getHospitalPick(String mber_id) {
		return joinDAO.getHospitalPick(mber_id);
	}
	
	// ���������� ���� �� ��� ��������
	@Override
	public List<HospitalVO> getNHPick(String mber_id) {
		return joinDAO.getNHPick(mber_id);
	}
	
	// �� ��� ����
	@Override
	public int removePick(String mber_id, String h_id) {
		return joinDAO.removePick(mber_id, h_id);
	}
	
	// ��纴�� �� ���� ��������
	@Override
	public int getHPickCount(String mber_id) {
		return joinDAO.getHPickCount(mber_id);
	}
	
	// ���� �� ���� ��������
	@Override
	public int getNPickCount(String mber_id) {
		return joinDAO.getNPickCount(mber_id);
	}
	
	

}
