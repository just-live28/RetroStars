package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.BoardConfig;
import dao.MemberDAO;
import dao.NBoardDAO;
import dto.NBoardDTO;


@WebServlet("*.nboard")
public class NBoardController extends HttpServlet {
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      NBoardDAO nManager = NBoardDAO.getInstance();
      MemberDAO mManager = MemberDAO.getInstance();
      
      String cmd = request.getRequestURI();
      
      try {
         if(cmd.equals("/write.nboard")) {
        
            
            String userId = (String)request.getSession().getAttribute("loginId");
            boolean isAdmin = mManager.isAdmin(userId);
         
            
            String nBoardTitle = request.getParameter("title");
            String nBoardContent = request.getParameter("contents");
            
            int result = nManager.insert(new NBoardDTO(0,userId,nBoardTitle,nBoardContent,null,0));
            
            System.out.println(userId);
            System.out.println(nBoardTitle);
            System.out.println(nBoardContent);
            
            response.sendRedirect("/list.nboard");
            
         } else if(cmd.equals("/list.nboard")) {
            System.out.println("list.nboard 들어감");

            
            String userId = (String)request.getSession().getAttribute("loginId");
            boolean isAdmin = mManager.isAdmin(userId);
        
            String pcpage = request.getParameter("cpage");
            
            if(pcpage==null) {pcpage="1";}
            int cpage = Integer.parseInt(pcpage);
            
            
            List<NBoardDTO> list = 
                  nManager.selectNtoM(
                  cpage*BoardConfig.recordCountPerPage-(BoardConfig.recordCountPerPage-1),
                  cpage*BoardConfig.recordCountPerPage
            );
            
            request.setAttribute("list", list); // 현재 내가 해당하는 페이지의 목록들.
            request.setAttribute("cpage", cpage);
            request.setAttribute("record_count_per_page", BoardConfig.recordCountPerPage);
            request.setAttribute("navi_count_per_page", BoardConfig.naviCountPerPage);
            request.setAttribute("record_total_count", nManager.getRecordCount());
            
            request.getRequestDispatcher("/nboard/nboardMain.jsp").forward(request,response);
         } else if(cmd.equals("/detail.nboard")) {
            
            System.out.println("detail.nboard진입");
            
            int seq = Integer.parseInt(request.getParameter("nBoardSeq"));
            NBoardDTO dto = nManager.detailPage(seq);
         
            request.setAttribute("dto",dto);

            nManager.updateViewCount(seq);

            request.getRequestDispatcher("/nboard/nboardDetail.jsp").forward(request, response);
         } else if(cmd.equals("/boardOut.nboard")) {
            int seq = Integer.parseInt(request.getParameter("nBoardSeq"));
            nManager.removePage(seq);
            response.sendRedirect("/list.nboard");
            

         } else if(cmd.equals("/update.nboard")) {
            System.out.println("update.nboard진입");
            int seq = Integer.parseInt(request.getParameter("seq"));
            String title = request.getParameter("title");
            String post = request.getParameter("post");
            int result = nManager.updateNBoard(new NBoardDTO(seq,null,title,post,null,0));
            response.sendRedirect("/detail.nboard?nBoardSeq="+seq);
            

         }else if(cmd.equals("/search.nboard")) {
        	    System.out.println("search.nboard진입");
        	    String keyword = request.getParameter("keyword");
        	    String filter = request.getParameter("filter");
        	    String pcpage = request.getParameter("cpage");
        	    
        	    if(pcpage == null) {
        	        pcpage = "1";
        	    }
        	    int cpage = Integer.parseInt(pcpage);
        	    
        	   
        	    List<NBoardDTO> searchResultList = 
                        nManager.selectNtoM(
                        cpage*BoardConfig.recordCountPerPage-(BoardConfig.recordCountPerPage-1),
                        cpage*BoardConfig.recordCountPerPage
                  );
        	    int totalRecordCount = 0;
        	    if(filter.equals("")) {
        	    	 searchResultList = nManager.searchListByTitle(keyword, 
             	            cpage * BoardConfig.recordCountPerPage - (BoardConfig.recordCountPerPage - 1),
             	            cpage * BoardConfig.recordCountPerPage);
             	        totalRecordCount = nManager.getSearchRecordCountByTitle(keyword);
        	    }
        	    else if(filter.equals("title")) {
        	        searchResultList = nManager.searchListByTitle(keyword, 
        	            cpage * BoardConfig.recordCountPerPage - (BoardConfig.recordCountPerPage - 1),
        	            cpage * BoardConfig.recordCountPerPage);
        	        totalRecordCount = nManager.getSearchRecordCountByTitle(keyword);
        	    } else if(filter.equals("post_number")) {
        	        searchResultList = nManager.searchListByPostNumber(keyword,
        	            cpage * BoardConfig.recordCountPerPage - (BoardConfig.recordCountPerPage - 1),
        	            cpage * BoardConfig.recordCountPerPage);
        	        totalRecordCount = nManager.getSearchRecordCountByPostNumber(keyword);
        	    }
        	   
        	    
        	    request.setAttribute("searchResult", searchResultList);
        	    request.setAttribute("cpage", cpage);
        	    request.setAttribute("record_count_per_page", BoardConfig.recordCountPerPage);
        	    request.setAttribute("navi_count_per_page", BoardConfig.naviCountPerPage);
        	    request.setAttribute("record_total_count", totalRecordCount);
        	    
        	    request.setAttribute("filter", filter);
        	    request.setAttribute("keyword", keyword);
        	    request.setAttribute("cpage",cpage);
        	    
        	    request.getRequestDispatcher("/nboard/nboardMain.jsp").forward(request, response);
        	}
         
      } catch(Exception e) {
         e.printStackTrace();
         System.out.println("Error during forward: " + e.getMessage());
      }
      
      
   
   }


   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      doGet(request, response);
   }

}
