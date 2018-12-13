

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson=new Gson();
		System.out.println("request:"+gson.toJson(request.getParameterMap()));
		
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		if(username==null ||username.equals("")||password==null||password.equals("")) {
			System.out.println("�û���������Ϊ��");
			return;
		}
		
		DBUtils dbUtils=new DBUtils();
		dbUtils.openConnect();
		BaseBean data=new BaseBean();
		UserBean userBean=new UserBean();
		if(dbUtils.isExistInDB(username, password)) {
			data.setCode(-1);
			data.setData(userBean);
			data.setMsg("���˺��Ѵ���");
			System.out.println("���˺��Ѵ���");
		}else if(!dbUtils.insertDataToDB(username, password)) {
			data.setCode(0);
			data.setMsg("ע��ɹ���");
			System.out.println("ע��ɹ���");
			ResultSet resultSet=dbUtils.getUser();
			int id=-1;
			if(resultSet!=null) {
				try {
					while (resultSet.next()) {
						if(resultSet.getString("user_name").equals(username)&&resultSet.getString("user_pwd").equals(password)) {
							id=resultSet.getInt("user_id");
							break;
						}
					}
					userBean.setId(id);
				} catch (SQLException  e) {
					// TODO: handle exception
				}
			}
			userBean.setUsername(username);
			userBean.setPassword(password);
			data.setData(userBean);
		}else {
			data.setCode(500);
	        data.setData(userBean);
	        data.setMsg("���ݿ����");
	        System.out.println("���ݿ����");
		}
		
		String json=gson.toJson(data);
		System.out.println("response:"+json);
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		try {
	        response.getWriter().print(json); // ��json���ݴ����ͻ���
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        response.getWriter().close(); // �ر����������Ȼ�ᷢ�������
	    }
		dbUtils.closeConnect();
	}
}
