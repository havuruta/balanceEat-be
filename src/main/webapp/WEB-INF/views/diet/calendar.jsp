<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- jQuery 추가 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>식단 달력</h2>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addDietModal">
            식단 추가하기
        </button>
    </div>
    <div id="calendar"></div>
</div>

<!-- 식단 추가 모달 -->
<div class="modal fade" id="addDietModal" tabindex="-1" aria-labelledby="addDietModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addDietModalLabel">식단 추가</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="addDietForm">
                    <div class="mb-3">
                        <label for="dietDate" class="form-label">날짜</label>
                        <input type="date" class="form-control" id="dietDate" name="dietDate" required>
                    </div>
                    <div class="mb-3">
                        <label for="mealType" class="form-label">식사 구분</label>
                        <select class="form-select" id="mealType" name="mealType" required>
                            <option value="BREAKFAST">아침</option>
                            <option value="LUNCH">점심</option>
                            <option value="DINNER">저녁</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="foodSearch" class="form-label">음식 검색</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="foodSearch" placeholder="음식 이름을 입력하세요">
                            <button class="btn btn-outline-secondary" type="button" onclick="openNutritionModal()">검색</button>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="amount" class="form-label">양 (g)</label>
                        <input type="number" class="form-control" id="amount" name="amount" required>
                    </div>
                    <div class="mb-3">
                        <label for="note" class="form-label">메모</label>
                        <textarea class="form-control" id="note" name="note" rows="3"></textarea>
                    </div>
                    <!-- 영양 정보 표시 영역 -->
                    <div id="nutritionInfo" class="mb-3" style="display: none;">
                        <h6>영양 정보</h6>
                        <div class="row">
                            <div class="col-md-3">
                                <p>칼로리: <span id="calories">0</span> kcal</p>
                            </div>
                            <div class="col-md-3">
                                <p>단백질: <span id="protein">0</span> g</p>
                            </div>
                            <div class="col-md-3">
                                <p>지방: <span id="fat">0</span> g</p>
                            </div>
                            <div class="col-md-3">
                                <p>탄수화물: <span id="carbohydrates">0</span> g</p>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="submitDiet()">저장</button>
            </div>
        </div>
    </div>
</div>

<!-- 영양 정보 검색 모달 -->
<div class="modal fade" id="nutritionModal" tabindex="-1" aria-labelledby="nutritionModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="nutritionModalLabel">영양 정보 검색</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <div class="input-group">
                        <input type="text" class="form-control" id="nutritionSearch" placeholder="음식 이름을 입력하세요">
                        <button class="btn btn-outline-secondary" type="button" onclick="searchNutrition()">검색</button>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>음식명</th>
                                <th>칼로리 (kcal)</th>
                                <th>단백질 (g)</th>
                                <th>지방 (g)</th>
                                <th>탄수화물 (g)</th>
                                <th>선택</th>
                            </tr>
                        </thead>
                        <tbody id="nutritionTableBody">
                            <!-- 검색 결과가 여기에 표시됩니다 -->
                        </tbody>
                    </table>
                </div>
                <div id="noResults" class="alert alert-info" style="display: none;">
                    검색 결과가 없습니다.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<!-- 식단 상세 정보 모달 -->
<div class="modal fade" id="dietDetailModal" tabindex="-1" aria-labelledby="dietDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="dietDetailModalLabel">식단 상세 정보</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-12">
                        <h6>아침</h6>
                        <div id="breakfastFoods" class="mb-2"></div>
                        <div class="row">
                            <div class="col-3">칼로리: <span id="detailBreakfastCalories">0</span>kcal</div>
                            <div class="col-3">단백질: <span id="detailBreakfastProtein">0</span>g</div>
                            <div class="col-3">지방: <span id="detailBreakfastFat">0</span>g</div>
                            <div class="col-3">탄수화물: <span id="detailBreakfastCarbohydrates">0</span>g</div>
                        </div>
                    </div>
                    <div class="col-12 mt-3">
                        <h6>점심</h6>
                        <div id="lunchFoods" class="mb-2"></div>
                        <div class="row">
                            <div class="col-3">칼로리: <span id="detailLunchCalories">0</span>kcal</div>
                            <div class="col-3">단백질: <span id="detailLunchProtein">0</span>g</div>
                            <div class="col-3">지방: <span id="detailLunchFat">0</span>g</div>
                            <div class="col-3">탄수화물: <span id="detailLunchCarbohydrates">0</span>g</div>
                        </div>
                    </div>
                    <div class="col-12 mt-3">
                        <h6>저녁</h6>
                        <div id="dinnerFoods" class="mb-2"></div>
                        <div class="row">
                            <div class="col-3">칼로리: <span id="detailDinnerCalories">0</span>kcal</div>
                            <div class="col-3">단백질: <span id="detailDinnerProtein">0</span>g</div>
                            <div class="col-3">지방: <span id="detailDinnerFat">0</span>g</div>
                            <div class="col-3">탄수화물: <span id="detailDinnerCarbohydrates">0</span>g</div>
                        </div>
                    </div>
                    <div class="col-12 mt-3">
                        <h6>일일 총합</h6>
                        <div class="row">
                            <div class="col-3">칼로리: <span id="detailTotalCalories">0</span>kcal</div>
                            <div class="col-3">단백질: <span id="detailTotalProtein">0</span>g</div>
                            <div class="col-3">지방: <span id="detailTotalFat">0</span>g</div>
                            <div class="col-3">탄수화물: <span id="detailTotalCarbohydrates">0</span>g</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="openEditDeleteModal('${props.summaryDate}')">수정/삭제</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<!-- 수정/삭제 모달 -->
<div class="modal fade" id="editDeleteModal" tabindex="-1" aria-labelledby="editDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editDeleteModalLabel">식단 수정/삭제</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>식사 구분</th>
                                <th>음식명</th>
                                <th>양 (g)</th>
                                <th>칼로리</th>
                                <th>단백질</th>
                                <th>지방</th>
                                <th>탄수화물</th>
                                <th>메모</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                        <tbody id="editDeleteTableBody">
                            <!-- 식단 목록이 여기에 표시됩니다 -->
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script>
let calendar; // 전역 변수로 선언

document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        events: function(info, successCallback, failureCallback) {
            // 현재 달력에 표시된 기간의 시작일과 종료일
            const start = info.start.toISOString();
            const end = info.end.toISOString();
            
            console.log('Fetching events for:', start, 'to', end); // 로그 추가
            
            // API 호출
            fetch('/diet/summaries?start=' + encodeURIComponent(start) + '&end=' + encodeURIComponent(end))
                .then(response => response.json())
                .then(data => {
                    console.log('Received data:', data);
                    data.forEach((diet, idx) => {
                        console.log(`diet[${idx}]:`, diet);
                    });
                    
                    // 데이터가 배열이 아닌 경우 빈 배열로 처리
                    if (!Array.isArray(data)) {
                        console.warn('서버에서 배열이 아닌 데이터가 반환되었습니다:', data);
                        successCallback([]);
                        return;
                    }
                    
                    // 데이터를 FullCalendar 이벤트 형식으로 변환
                    const events = data.map(summary => {
                        // 각 식사 시간대별 상태 확인 (칼로리로 판단)
                        const hasBreakfast = summary.breakfastCalories > 0;
                        const hasLunch = summary.lunchCalories > 0;
                        const hasDinner = summary.dinnerCalories > 0;
                        
                        console.log('Summary for', summary.summaryDate, ':', {
                            breakfast: hasBreakfast,
                            lunch: hasLunch,
                            dinner: hasDinner,
                            calories: {
                                breakfast: summary.breakfastCalories,
                                lunch: summary.lunchCalories,
                                dinner: summary.dinnerCalories
                            }
                        });
                        
                        // 식사 상태를 이모지로 표시
                        const breakfastEmoji = hasBreakfast ? "🍳" : "❌";
                        const lunchEmoji = hasLunch ? "🍚" : "❌";
                        const dinnerEmoji = hasDinner ? "🍖" : "❌";
                        
                        const title = breakfastEmoji + lunchEmoji + dinnerEmoji;
                        console.log('Generated title:', title); // 로그 추가
                        
                        const event = {
                            title: title,
                            start: summary.summaryDate,
                            allDay: true,
                            extendedProps: {
                                breakfast: summary.breakfast,
                                breakfastCalories: summary.breakfastCalories,
                                breakfastProtein: summary.breakfastProtein,
                                breakfastFat: summary.breakfastFat,
                                breakfastCarbohydrates: summary.breakfastCarbohydrates,
                                lunch: summary.lunch,
                                lunchCalories: summary.lunchCalories,
                                lunchProtein: summary.lunchProtein,
                                lunchFat: summary.lunchFat,
                                lunchCarbohydrates: summary.lunchCarbohydrates,
                                dinner: summary.dinner,
                                dinnerCalories: summary.dinnerCalories,
                                dinnerProtein: summary.dinnerProtein,
                                dinnerFat: summary.dinnerFat,
                                dinnerCarbohydrates: summary.dinnerCarbohydrates,
                                totalCalories: summary.totalCalories,
                                totalProtein: summary.totalProtein,
                                totalFat: summary.totalFat,
                                totalCarbohydrates: summary.totalCarbohydrates
                            }
                        };
                        
                        console.log('Created event:', event); // 로그 추가
                        return event;
                    });
                    
                    console.log('Final events:', events); // 로그 추가
                    successCallback(events);
                })
                .catch(error => {
                    console.error('Error fetching diet summaries:', error);
                    failureCallback(error);
                });
        },
        eventClick: function(info) {
            const props = info.event.extendedProps;
            const eventDate = info.event.start;
            
            // 음식 목록 표시
            const breakfastFoods = props.breakfast ? props.breakfast.map(d => d.foodName).join(', ') : '없음';
            const lunchFoods = props.lunch ? props.lunch.map(d => d.foodName).join(', ') : '없음';
            const dinnerFoods = props.dinner ? props.dinner.map(d => d.foodName).join(', ') : '없음';
            
            document.getElementById('breakfastFoods').textContent = breakfastFoods;
            document.getElementById('lunchFoods').textContent = lunchFoods;
            document.getElementById('dinnerFoods').textContent = dinnerFoods;
            
            // 영양 정보 표시
            document.getElementById('detailBreakfastCalories').textContent = props.breakfastCalories;
            document.getElementById('detailBreakfastProtein').textContent = props.breakfastProtein;
            document.getElementById('detailBreakfastFat').textContent = props.breakfastFat;
            document.getElementById('detailBreakfastCarbohydrates').textContent = props.breakfastCarbohydrates;
            
            document.getElementById('detailLunchCalories').textContent = props.lunchCalories;
            document.getElementById('detailLunchProtein').textContent = props.lunchProtein;
            document.getElementById('detailLunchFat').textContent = props.lunchFat;
            document.getElementById('detailLunchCarbohydrates').textContent = props.lunchCarbohydrates;
            
            document.getElementById('detailDinnerCalories').textContent = props.dinnerCalories;
            document.getElementById('detailDinnerProtein').textContent = props.dinnerProtein;
            document.getElementById('detailDinnerFat').textContent = props.dinnerFat;
            document.getElementById('detailDinnerCarbohydrates').textContent = props.dinnerCarbohydrates;
            
            document.getElementById('detailTotalCalories').textContent = props.totalCalories;
            document.getElementById('detailTotalProtein').textContent = props.totalProtein;
            document.getElementById('detailTotalFat').textContent = props.totalFat;
            document.getElementById('detailTotalCarbohydrates').textContent = props.totalCarbohydrates;
            
            // 모달 표시
            const dietDetailModal = new bootstrap.Modal(document.getElementById('dietDetailModal'));
            dietDetailModal.show();
            
            // 수정/삭제 버튼에 날짜 정보 저장
            const modifyButton = document.querySelector('#dietDetailModal .btn-primary');
            modifyButton.onclick = function() {
                openEditDeleteModal(eventDate.toISOString());
            };
        }
    });
    calendar.render();
    
    // 오늘 날짜를 기본값으로 설정
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    document.getElementById('dietDate').value = formattedDate;
});

let selectedNutrition = null;

function openNutritionModal() {
    // 영양 정보 모달 열기
    const nutritionModal = new bootstrap.Modal(document.getElementById('nutritionModal'));
    nutritionModal.show();
    
    // 검색어가 있으면 자동으로 검색
    const searchTerm = document.getElementById('foodSearch').value.trim();
    if (searchTerm) {
        document.getElementById('nutritionSearch').value = searchTerm;
        searchNutrition();
    } else {
        // 검색어가 없으면 전체 목록 표시
        fetch('/nutrition/all')
            .then(response => response.json())
            .then(data => {
                console.log('전체 목록:', data.length, '개 항목');
                displayNutritionResults(data);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('영양 정보 검색 중 오류가 발생했습니다.');
            });
    }
}

function searchNutrition() {
    const searchTerm = document.getElementById('nutritionSearch').value.trim();
    
    // 검색어가 비어있는지 확인
    if (!searchTerm) {
        // 검색어가 없으면 전체 목록 표시
        fetch('/nutrition/all')
            .then(response => response.json())
            .then(data => displayNutritionResults(data))
            .catch(error => {
                console.error('Error:', error);
                alert('영양 정보 검색 중 오류가 발생했습니다.');
            });
        return;
    }

    // 검색어가 있는 경우 검색 API 호출
    fetch('/nutrition/search?name=' + encodeURIComponent(searchTerm))
        .then(response => response.json())
        .then(data => {
            console.log('검색 결과:', data.length, '개 항목');
            displayNutritionResults(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('영양 정보 검색 중 오류가 발생했습니다.');
        });
}

function displayNutritionResults(data) {
    const tableBody = document.getElementById('nutritionTableBody');
    const noResults = document.getElementById('noResults');
    tableBody.innerHTML = '';
    
    // 데이터가 배열이 아닌 경우 빈 배열로 처리
    if (!Array.isArray(data)) {
        console.warn('서버에서 배열이 아닌 데이터가 반환되었습니다:', data);
        data = [];
    }
    
    if (!data || data.length === 0) {
        tableBody.style.display = 'none';
        noResults.style.display = 'block';
        noResults.textContent = '검색 결과가 없습니다. 다른 검색어를 입력해보세요.';
    } else {
        tableBody.style.display = 'table-row-group';
        noResults.style.display = 'none';
        
        // 최대 50개 항목만 표시
        const displayData = data.length > 50 ? data.slice(0, 50) : data;
        
        displayData.forEach(food => {
            const row = document.createElement('tr');
            
            // 템플릿 리터럴 대신 문자열 연결 사용
            row.innerHTML = 
                '<td>' + food.name + '</td>' +
                '<td>' + food.calories + '</td>' +
                '<td>' + food.protein + '</td>' +
                '<td>' + food.fat + '</td>' +
                '<td>' + food.carbohydrates + '</td>' +
                '<td>' +
                '<button class="btn btn-sm btn-primary" onclick="selectNutrition(' + 
                JSON.stringify(food).replace(/"/g, '&quot;') + 
                ')">선택</button>' +
                '</td>';
                
            tableBody.appendChild(row);
        });
        
        // 50개 이상인 경우 메시지 표시
        if (data.length > 50) {
            const infoRow = document.createElement('tr');
            infoRow.innerHTML = 
                '<td colspan="6" class="text-center">' +
                '<div class="alert alert-info">' +
                '검색 결과가 많습니다. 더 정확한 검색어를 입력하세요. (총 ' + data.length + '개 중 50개 표시)' +
                '</div>' +
                '</td>';
            tableBody.appendChild(infoRow);
        }
    }
}

function selectNutrition(food) {
    selectedNutrition = food;
    document.getElementById('foodSearch').value = food.name;
    
    // 영양 정보 표시
    document.getElementById('nutritionInfo').style.display = 'block';
    document.getElementById('calories').textContent = food.calories;
    document.getElementById('protein').textContent = food.protein;
    document.getElementById('fat').textContent = food.fat;
    document.getElementById('carbohydrates').textContent = food.carbohydrates;
    
    // 영양 정보 모달 닫기
    bootstrap.Modal.getInstance(document.getElementById('nutritionModal')).hide();
}

function submitDiet() {
    if (!selectedNutrition) {
        alert('음식을 선택해주세요.');
        return;
    }

    const amount = document.getElementById('amount').value;
    if (!amount || amount <= 0) {
        alert('올바른 양을 입력해주세요.');
        return;
    }

    // 양에 따른 영양소 계산
    const ratio = amount / 100; // 100g 기준으로 계산
    const calculatedNutrition = {
        calories: selectedNutrition.calories * ratio,
        protein: selectedNutrition.protein * ratio,
        fat: selectedNutrition.fat * ratio,
        carbohydrates: selectedNutrition.carbohydrates * ratio
    };

    const formData = {
        dietDate: document.getElementById('dietDate').value,
        mealType: document.getElementById('mealType').value,
        foodName: selectedNutrition.name,
        amount: amount,
        note: document.getElementById('note').value,
        calories: calculatedNutrition.calories,
        protein: calculatedNutrition.protein,
        fat: calculatedNutrition.fat,
        carbohydrates: calculatedNutrition.carbohydrates
    };

    fetch('/diet/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.status === 401) {
            alert('로그인이 필요합니다. 로그인 페이지로 이동합니다.');
            window.location.href = '/auth/login';
            throw new Error('로그인이 필요합니다.');
        }
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || 'Network response was not ok');
            });
        }
        return response.text();
    })
    .then(data => {
        console.log('서버 응답:', data);
        alert(data);
        $('#addDietModal').modal('hide');
        
        // 캘린더 새로고침
        if (calendar && typeof calendar.refetchEvents === 'function') {
            calendar.refetchEvents();
        } else {
            console.log('캘린더를 찾을 수 없거나 새로고침할 수 없습니다.');
            location.reload(); // 페이지 새로고침으로 대체
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error.message || '식단 추가 중 오류가 발생했습니다.');
    });
}

function openEditDeleteModal(date) {
    console.log('Opening edit/delete modal for date:', date);
    
    // 날짜에 하루를 더하고 형식 변환
    const eventDate = new Date(date);
    eventDate.setDate(eventDate.getDate() + 1);
    const formattedDate = eventDate.toISOString().split('T')[0];
    console.log('Original date:', date);
    console.log('Adjusted date:', formattedDate);
    
    // 서버에서 해당 날짜의 식단 데이터 가져오기
    fetch('/diet/edit?date=' + formattedDate)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Received data:', data);
            data.forEach((diet, idx) => {
                console.log('diet:', diet);
                console.log('id:', diet.id);
                console.log('foodName:', diet.foodName);
                console.log('amount:', diet.amount);
                console.log('calories:', diet.calories);
            });
            
            // 데이터가 배열이 아닌 경우 처리
            if (!Array.isArray(data)) {
                console.warn('Received data is not an array:', data);
                data = [];
            }
            
            const tbody = document.getElementById('editDeleteTableBody');
            tbody.innerHTML = '';
            
            if (!Array.isArray(data) || data.length === 0) {
                tbody.innerHTML =
                '<tr><td colspan="9" class="text-center">해당 날짜의 식단이 없습니다.</td></tr>';
                return;
            }
            
            data.forEach(d => {
                const tr = document.createElement('tr');
                tr.setAttribute('data-id', d.id);
                console.log('Created row with data-id:', d.id);

                // 셀 0: 식사 구분
                tr.insertCell().textContent =
                d.mealType === 'BREAKFAST' ? '아침' :
                d.mealType === 'LUNCH'     ? '점심' : '저녁';

                // 셀 1: 음식명
                tr.insertCell().textContent = d.foodName ?? '';

                // 셀 2: 양(g) - input
                const amtTd = tr.insertCell();
                const amtIn = document.createElement('input');
                amtIn.type = 'number';
                amtIn.className = 'form-control form-control-sm';
                amtIn.value = d.amount ?? 0;
                amtIn.id = `amount_${d.id}`;
                amtTd.appendChild(amtIn);

                // 셀 3~6: 영양소
                tr.insertCell().textContent = (d.calories      ?? '').toFixed?.(1) ?? '';
                tr.insertCell().textContent = (d.protein       ?? '').toFixed?.(1) ?? '';
                tr.insertCell().textContent = (d.fat           ?? '').toFixed?.(1) ?? '';
                tr.insertCell().textContent = (d.carbohydrates ?? '').toFixed?.(1) ?? '';

                // 셀 7: 메모
                tr.insertCell().textContent = d.note ?? '';

                // 셀 8: 수정/삭제 버튼
                const actTd = tr.insertCell();
                const updateButton = document.createElement('button');
                updateButton.className = 'btn btn-sm btn-primary me-1';
                updateButton.textContent = '수정';
                updateButton.onclick = function() { 
                    console.log('Update button clicked, diet ID:', d.id);
                    updateDiet(d.id); 
                };
                
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.textContent = '삭제';
                deleteButton.onclick = function() { 
                    console.log('Delete button clicked, diet ID:', d.id);
                    deleteDiet(d.id); 
                };
                
                actTd.appendChild(updateButton);
                actTd.appendChild(deleteButton);

                tbody.appendChild(tr);
            });

            document.getElementById('editDeleteModalLabel').textContent =
                formattedDate + ' 식단 수정/삭제';
            new bootstrap.Modal('#editDeleteModal').show();
            })
            .catch(err => {
            console.error(err);
            document.getElementById('editDeleteTableBody').innerHTML =
                '<tr><td colspan="9" class="text-danger text-center">데이터 로딩 실패</td></tr>';
    });
}

function updateDiet(dietId) {
    console.log('updateDiet called with ID:', dietId, 'Type:', typeof dietId);
    
    // ID가 문자열로 들어오는 경우를 처리
    const numericId = parseInt(dietId, 10);
    if (isNaN(numericId)) {
        console.error('Invalid diet ID:', dietId);
        alert('식단 ID가 유효하지 않습니다.');
        return;
    }

    const amountInput = document.getElementById(`amount_${numericId}`);
    if (!amountInput) {
        console.error('Amount input not found for ID:', numericId);
        alert('양을 입력할 수 없습니다.');
        return;
    }

    const newAmount = amountInput.value;
    console.log('New amount:', newAmount);
    
    if (!newAmount || isNaN(newAmount) || newAmount <= 0) {
        alert('올바른 양을 입력해주세요.');
        return;
    }

    const requestBody = {
        amount: parseFloat(newAmount)
    };
    console.log('Request body:', requestBody);

    const url = `/diet/update/${numericId}`;
    console.log('Request URL:', url);

    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || '식단 수정 중 오류가 발생했습니다.');
            });
        }
        return response.text();
    })
    .then(data => {
        alert(data);
        // 캘린더 새로고침
        if (calendar && typeof calendar.refetchEvents === 'function') {
            calendar.refetchEvents();
        }
        // 수정/삭제 모달 새로고침
        const currentDate = document.querySelector('#editDeleteModal .modal-title').textContent.split(' ')[0];
        openEditDeleteModal(currentDate);
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error.message);
    });
}

function deleteDiet(dietId) {
    console.log('deleteDiet called with ID:', dietId, 'Type:', typeof dietId);
    
    // ID가 문자열로 들어오는 경우를 처리
    const numericId = parseInt(dietId, 10);
    if (isNaN(numericId)) {
        console.error('Invalid diet ID:', dietId);
        alert('식단 ID가 유효하지 않습니다.');
        return;
    }

    if (!confirm('정말로 이 식단을 삭제하시겠습니까?')) {
        return;
    }

    fetch('/diet/delete/' + numericId, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || '식단 삭제 중 오류가 발생했습니다.');
            });
        }
        return response.text();
    })
    .then(data => {
        alert(data);
        // 캘린더 새로고침
        if (calendar && typeof calendar.refetchEvents === 'function') {
            calendar.refetchEvents();
        }
        // 수정/삭제 모달 새로고침
        const currentDate = document.querySelector('#editDeleteModal .modal-title').textContent.split(' ')[0];
        openEditDeleteModal(currentDate);
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error.message);
    });
}
</script>

<%@ include file="../layout/footer.jsp" %> 