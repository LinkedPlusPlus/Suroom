from django.urls import path, include
from . import views

urlpatterns = [
    path('join/', views.user_list),
    path('join/<int:pk>/', views.user_detail),
    path('sign_in/', views.user_login),
    path('choice/tendency/', views.choice_tendency),
    path('group/find/', views.FindGroup.as_view()),
    path('group/find/<int:pk>/', views.FindGroupDetail.as_view()),
    path('group/', views.group_list.as_view()),
    path('group/<int:pk>/', views.group_detail.as_view()),
    path('group/join/', views.join_group),
    path('group/isJoin/<int:user_pk>/<int:group_pk>/', views.UserGroupList.as_view()),
    path('group/u/<int:pk>/', views.UserGroupListUser.as_view()),
    path('group/album/', views.album_list.as_view()),
    path('group/search/', views.GroupSearch.as_view()),
    path('group/update/<str:category>/', views.GroupUpdate.as_view()),

    path('group/<int:group_pk>/planner/', views.planner_list.as_view()),
    path('group/planner/create/', views.planner_list.as_view()),
    path('group/planner/<int:pk>/delete', views.planner_detail.as_view()),
]
