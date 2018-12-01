from rest_framework import serializers
from .models import User, Group, Tendency, Planner
from .models import User_Group, User_Tendency
from .models import Wait


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'

class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = '__all__'

class TendencySerializer(serializers.ModelSerializer):
    class Meta:
        model = Tendency
        fields = '__all__'

class UserGroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = User_Group
        fields = '__all__'

class UserTendencySerializer(serializers.ModelSerializer):
    class Meta:
        model = User_Tendency
        fields = '__all__'

class WaitSerializer(serializers.ModelSerializer):
    class Meta:
        model = Wait
        fields = '__all__'


class PlannerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Planner
        fields = '__all__'