<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 탈퇴</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
        }
        
        .container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 1rem;
            text-align: center;
        }
        
        .warning-container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 2rem;
        }
        
        .warning {
            color: #f44336;
            margin: 20px 0;
        }
        
        .btn-group {
            display: flex;
            justify-content: center;
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
        
        .btn-delete {
            background-color: #f44336;
            color: white;
        }
        
        .btn-cancel {
            background-color: #4CAF50;
            color: white;
        }
    </style>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
    <div class="container">
        <h1>회원 탈퇴</h1>
        <div class="warning-container">
            <div class="warning">
                <h2>정말로 탈퇴하시겠습니까?</h2>
                <p>탈퇴 후에는 모든 정보가 삭제되며 복구할 수 없습니다.</p>
                <p>탈퇴 후에는 해당 계정으로 로그인할 수 없습니다.</p>
            </div>
            <form action="/user/delete" method="post">
                <div class="btn-group">
                    <button type="submit" class="btn btn-delete">탈퇴하기</button>
                    <a href="/user/mypage" class="btn btn-cancel">취소</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html> 