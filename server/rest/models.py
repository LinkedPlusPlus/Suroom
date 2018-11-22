from django.db import models
from django.utils import timezone

# Create your models here.

'''
class User_info(models.Model):
    first_name = models.CharField(max_length=20)
    last_name = models.CharField(max_length=20)
    sex = models.IntegerField(default = 0)      # 0 : male, 1 : female
    age = models.IntegerField()
    email = models.EmailField()
    join_date = models.DateTimeField(blank=True, null=True)

    def join(self):
        self.join_date = timezone.now()
        self.save()

    def __str__(self):
        return ("")
'''

# User와 Group은 수정 할 시 안드로이드의 MySetting도 수정해줘야 함.
class User(models.Model):
    auth_id = models.CharField(max_length=20, unique = True)
    auth_pw = models.CharField(max_length=20)

class Group(models.Model):
    name = models.CharField(max_length=30)
    description = models.CharField(max_length=40, blank=True, null=True)
    master = models.ForeignKey(User, on_delete=models.DO_NOTHING, null=True)
    public = models.NullBooleanField(default=True)
    max_num_people = models.IntegerField(default = 10)
    num_people = models.IntegerField(default = 0)
    tag1 = models.CharField(max_length = 20, blank=True, null=True)
    tag2 = models.CharField(max_length = 20, blank=True, null=True)
    tag3 = models.CharField(max_length = 20, blank=True, null=True)
    tag4 = models.CharField(max_length = 20, blank=True, null=True)
    tag5 = models.CharField(max_length = 20, blank=True, null=True)
    created_date = models.DateTimeField(default=timezone.now)
    notification = models.TextField(null=True)
    meeting = models.CharField(max_length = 40, null=True)
    
class User_Group(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    group = models.ForeignKey(Group, on_delete=models.CASCADE)
    joined = models.DateTimeField(default=timezone.now)
    role = models.IntegerField(default=0)

    class Meta:
        unique_together = (("user", "group"),)

class Tendency(models.Model):
    name = models.CharField(max_length=20)

class User_Tendency(models.Model):
    user= models.ForeignKey(User, on_delete=models.CASCADE)
    rule = models.SmallIntegerField(default=1)
    learning = models.SmallIntegerField(default=1)
    numberPeople = models.SmallIntegerField(default=1)
    friendship = models.SmallIntegerField(default=1)
    environment = models.SmallIntegerField(default=1)
    style = models.SmallIntegerField(default=1)

class Wait(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    start_time = models.DateTimeField(default=timezone.now)