from django.contrib import admin

from .models import User, Group, Tendency
from .models import User_Group, User_Tendency
from .models import Wait, Album

@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = ['id', 'auth_id', 'auth_pw']
    list_display_links = ['id']


@admin.register(Group)
class GroupAdmin(admin.ModelAdmin):
    list_display = ['id', 'name', 'description']
    list_display_links = ['id']

@admin.register(User_Group)
class UserGroupAdmin(admin.ModelAdmin):
    list_display = ['id', 'user', 'group', 'joined', 'role']
    list_display_links = ['id']



@admin.register(Tendency)
class TendencyAdmin(admin.ModelAdmin):
    list_display = ['id', 'name']
    list_display_links = ['id']

@admin.register(User_Tendency)
class UserTendencyAdmin(admin.ModelAdmin):
    list_display = ['id', 'user_id', 'rule', 'learning', 'numberPeople', 'friendship', 'environment', 'style']
    list_display_links = ['id']

@admin.register(Wait)
class WaitAdmin(admin.ModelAdmin):
    list_display = ['id', 'user', 'start_time']
    list_display_links = ['id']

@admin.register(Album)
class AlbumAdmin(admin.ModelAdmin):
    list_display = ['id', 'group', 'image']
    list_display_links = ['id']

'''
admin.site.register(User)
admin.site.register(Group)
admin.site.register(Tendency)
admin.site.register(User_Group)
admin.site.register(User_Tendency)
'''
