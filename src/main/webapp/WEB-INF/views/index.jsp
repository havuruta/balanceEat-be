<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="layout/header.jsp" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12 p-0">
            <!-- 히어로 섹션 -->
            <div class="hero-section position-relative" style="height: 500px; background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://images.unsplash.com/photo-1498837167922-ddd27525d352?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80'); background-size: cover; background-position: center;">
                <div class="position-absolute top-50 start-50 translate-middle text-center text-white">
                    <h1 class="display-3 fw-bold mb-4">BalanceEat</h1>
                    <p class="lead mb-4">균형 잡힌 식습관으로 건강한 삶을 시작하세요</p>
                    <a href="/nutrition/list" class="btn btn-success btn-lg">영양 정보 보기</a>
                </div>
            </div>
        </div>
    </div>

    <!-- 특징 섹션 -->
    <div class="row py-5 bg-light">
        <div class="col-md-12 text-center mb-4">
            <h2 class="fw-bold">BalanceEat의 특징</h2>
        </div>
        <div class="col-md-4 text-center">
            <div class="p-4">
                <i class="fas fa-chart-pie fa-3x text-success mb-3"></i>
                <h4>영양 정보 관리</h4>
                <p>음식의 영양 정보를 쉽게 조회하고 관리할 수 있습니다.</p>
            </div>
        </div>
        <div class="col-md-4 text-center">
            <div class="p-4">
                <i class="fas fa-user-shield fa-3x text-success mb-3"></i>
                <h4>개인화된 서비스</h4>
                <p>회원가입을 통해 개인화된 영양 정보 관리가 가능합니다.</p>
            </div>
        </div>
        <div class="col-md-4 text-center">
            <div class="p-4">
                <i class="fas fa-mobile-alt fa-3x text-success mb-3"></i>
                <h4>모바일 친화적</h4>
                <p>어디서든 쉽게 접근할 수 있는 반응형 디자인을 제공합니다.</p>
            </div>
        </div>
    </div>

    <!-- 통계 섹션 -->
    <div class="row py-5">
        <div class="col-md-12 text-center mb-4">
            <h2 class="fw-bold">우리의 성과</h2>
        </div>
        <div class="col-md-3 text-center">
            <div class="p-4">
                <h3 class="text-success fw-bold">1000+</h3>
                <p>등록된 음식</p>
            </div>
        </div>
        <div class="col-md-3 text-center">
            <div class="p-4">
                <h3 class="text-success fw-bold">500+</h3>
                <p>활성 사용자</p>
            </div>
        </div>
        <div class="col-md-3 text-center">
            <div class="p-4">
                <h3 class="text-success fw-bold">50+</h3>
                <p>영양 정보 카테고리</p>
            </div>
        </div>
        <div class="col-md-3 text-center">
            <div class="p-4">
                <h3 class="text-success fw-bold">24/7</h3>
                <p>지원 서비스</p>
            </div>
        </div>
    </div>

    <!-- CTA 섹션 -->
    <div class="row py-5 bg-success text-white">
        <div class="col-md-12 text-center">
            <h2 class="fw-bold mb-4">지금 바로 시작하세요</h2>
            <p class="lead mb-4">건강한 식습관을 위한 첫 걸음을 BalanceEat과 함께하세요</p>
            <a href="/auth/register" class="btn btn-light btn-lg">무료 회원가입</a>
        </div>
    </div>
</div>

<!-- Font Awesome 추가 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<!-- 애니메이션 효과를 위한 CSS -->
<style>
    .hero-section {
        transition: all 0.3s ease;
    }
    .hero-section:hover {
        transform: scale(1.02);
    }
    .btn {
        transition: all 0.3s ease;
    }
    .btn:hover {
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }
    .fas {
        transition: all 0.3s ease;
    }
    .fas:hover {
        transform: scale(1.2);
        color: #28a745;
    }
</style>

<%@ include file="layout/footer.jsp" %> 