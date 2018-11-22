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
]