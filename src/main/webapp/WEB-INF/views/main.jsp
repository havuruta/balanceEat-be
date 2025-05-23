<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BalanceEat - 메인</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
        }
        
        .main-content {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        
        .welcome-section {
            text-align: center;
            padding: 2rem;
            background-color: #f5f5f5;
            border-radius: 8px;
            margin-bottom: 2rem;
        }
        
        .features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 1rem;
        }
        
        .feature-card {
            padding: 1.5rem;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/layout/header.jsp" %>
    
    <div class="main-content">
        <div class="welcome-section">
            <h1>BalanceEat에 오신 것을 환영합니다</h1>
            <p>건강한 식습관을 위한 최고의 파트너</p>
        </div>
        
        <div class="features">
            <div class="feature-card">
                <h2>맞춤형 식단 추천</h2>
                <p>당신의 건강 상태와 목표에 맞는 최적의 식단을 추천해드립니다.</p>
            </div>
            <div class="feature-card">
                <h2>영양 정보 분석</h2>
                <p>섭취한 음식의 영양 정보를 상세히 분석해드립니다.</p>
            </div>
            <div class="feature-card">
                <h2>건강 관리</h2>
                <p>체중, 운동, 수면 등 건강 관련 정보를 종합적으로 관리할 수 있습니다.</p>
            </div>
        </div>
    </div>
</body>
</html> 