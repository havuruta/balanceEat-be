<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <style>
        .user-info {
            border: 1px solid #ddd;
            padding: 20px;
            margin-bottom: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .btn-group {
            margin-top: 20px;
            display: flex;
            gap: 1rem;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }
        
        .btn-update {
            background-color: #4CAF50;
            color: white;
        }
        
        .btn-delete {
            background-color: #f44336;
            color: white;
        }
    </style>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
    
    <div class="user-info">
        <h2>회원 정보</h2>
        <p><strong>아이디:</strong> ${user.username}</p>
    </div>
    <div class="btn-group">
        <a href="/user/update" class="btn btn-update">정보 수정</a>
        <a href="/user/delete" class="btn btn-delete">회원 탈퇴</a>
    </div>
</body>
</html> 