<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 완료</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://webfontworld.github.io/daegu/DalseoHealing.css">
</head>
<body>
         <video class="video-background" autoplay muted loop>
           <source src="/image/video.mp4" type="video/mp4">
           Your browser does not support the video tag.
       </video>
    <div class="container">
        <img src="/image/logo.png" alt="Logo" class="logo">
        <h1>회원가입 완료</h1>
        <p>Retro Stars 회원가입이 완료되었습니다.</p>
        <form action="/registerComplete.member" method="post">
            <button type="submit">확인</button>
        </form>
    </div>
</body>
</html>