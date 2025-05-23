<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BalanceEat - 영양 정보 관리</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.css' rel='stylesheet' />
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.js'></script>
    <style>
        .navbar-brand {
            font-weight: bold;
            color: #28a745 !important;
        }
        .nav-link {
            color: #495057;
        }
        .nav-link:hover {
            color: #28a745;
        }
        .sidebar {
            min-height: calc(100vh - 56px);
            background-color: #f8f9fa;
            padding: 20px;
        }
        .sidebar .nav-link {
            color: #495057;
            padding: 8px 16px;
            margin: 4px 0;
            border-radius: 4px;
        }
        .sidebar .nav-link:hover {
            background-color: #e9ecef;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">BalanceEat</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/nutrition/list">영양 정보 조회</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/diet/calendar">나의 식단 달력</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/login">로그인</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/register">회원가입</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <span class="nav-link">${sessionScope.user.username}님 환영합니다</span>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/user/mypage">마이페이지</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/logout">로그아웃</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-2 sidebar">
                <h5 class="mb-3">메뉴</h5>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="/nutrition/list">영양 정보 조회</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/diet/calendar">나의 식단 달력</a>
                    </li>
                    <c:if test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="/user/mypage">마이페이지</a>
                        </li>
                    </c:if>
                </ul>
            </div>
            <div class="col-md-10">
                <div class="container mt-4"> 