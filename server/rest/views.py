# Create your views here.

from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from rest_framework.decorators import api_view
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from .models import User, Group, Tendency
from .models import User_Group, User_Tendency
from .models import Wait, Album, Planner
from .serializers import UserSerializer, UserTendencySerializer
from. serializers import WaitSerializer, GroupSerializer, UserGroupSerializer, AlbumSerializer, PlannerSerializer
from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from django.http import Http404
from django.db.models import Q
import csv

# Create your views here.

def index(request):
    return render(request, 'rest/index.html', {})

@api_view(['GET', 'POST'])
def user_list(request):
    if request.method == 'GET':
        users = User.objects.all()
        serializer = UserSerializer(users, many=True)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = UserSerializer(data = data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)

@api_view(['GET', 'PUT', 'DELETE'])
def user_detail(request, pk):
    try:
        user = User.objects.get(pk=pk)
    except(User.DoesNotExist):
        return (HttpResponse(status=404))

    if (request.method == 'GET'):
        serializer = UserSerializer(user)
        return (JsonResponse(serializer.data))
    
    elif (request.method == 'PUT'):
        data = JSONParser().parse(request)
        serializer = UserSerializer(user, data=data)
        if serializer.is_valid():
            serializer.save()
            return (JsonResponse(serializer.data))
        return (JsonResponse(serializer.errors, status=404))

    elif request.method == 'DELETE':
        user.delete()
        return (HttpResponse(status=204))
@api_view(['POST'])
def user_login(request):
    if (request.method == 'POST'):
        data = JSONParser().parse(request)
        try:
            user = User.objects.filter(auth_id = data['auth_id'])
            user = user[0]
            if (user.auth_pw == data['auth_pw']):
                serializer = UserSerializer(user)
                resData = serializer.data
                try:
                    ut = User_Tendency.objects.get(user_id = user)
                    resData['rule'] = ut.rule
                    resData['learning'] = ut.learning
                    resData['numberPeople'] = ut.numberPeople
                    resData['friendship'] = ut.friendship
                    resData['environment'] = ut.environment
                    resData['style'] = ut.style
                except:
                    resData['rule'] = 1
                    resData['learning'] = 1
                    resData['numberPeople'] = 1
                    resData['friendship'] = 1
                    resData['environment'] = 1
                    resData['style'] = 1
                return (Response(data=resData, status=status.HTTP_200_OK))
            else:
                return (Response(status=status.HTTP_404_NOT_FOUND))
        except(User.DoesNotExist):
            return (Response(status=status.HTTP_404_NOT_FOUND))

@api_view(['POST', 'GET'])
def choice_tendency(request):
    if (request.method=='POST'):
        data = JSONParser().parse(request)
        user_id = data['id']
        user = User.objects.get(pk=user_id)

        try:
            queryset = User_Tendency.objects.filter(user_id=user)
            queryset.delete()
        except:
            print('user(',user_id,') choose tendencies.')

        try:
            insert = User_Tendency.objects.create(user_id=user, rule=data['규칙'], learning=data['학습량'], \
            numberPeople=data['인원'], friendship=data['친목'], environment=data['환경'], style=data['스타일'])
        except:
            return Response(status=status.HTTP_406_NOT_ACCEPTABLE)
        return Response(status=status.HTTP_200_OK)

    elif (request.method=='GET'):
        user_tendency = User_Tendency.objects.all()
        serializer = UserTendencySerializer(user_tendency, many=True)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    else:
        return Response(status=status.HTTP_404_NOT_FOUND)

# 매칭 구현을 위한 뷰 (사용 할지 말지는 미지수)
class FindGroup(APIView):
    def get(self, request):
        waiter = Wait.objects.all()
        serializer = WaitSerializer(waiter, many=True)
        return Response(serializer.data)
    
    def post(self, request):
        data = JSONParser().parse(request)
        try:
            user = User.objects.get(pk=data['id'])
        except User.DoesNotExist:
            return Http404
        Wait.objects.filter(user=user).delete()
        Wait.objects.create(user=user)
        return Response(status=status.HTTP_201_CREATED)

# 매칭 구현을 위한 뷰 (사용 할지 말지는 미지수)
class FindGroupDetail(APIView):
    def get_object(self, pk):
        try:
            return Wait.objects.get(pk=pk)
        except Wait.DoesNotExist:
            return Http404

    def get(self, request, pk, format=None):
        waiter = self.get_object(pk)
        serializer = WaitSerializer(waiter)
        return Response(serializer.data)

    def delete(self, request, pk, format=None):
        waiter = self.get_object(pk)
        waiter.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

# 방 목록, 생성 클래스
class group_list(APIView):
    def get(self, request):
        groups = Group.objects.filter(public=True).order_by("-created_date")
        serializer = GroupSerializer(groups, many=True)
        return Response(serializer.data)

    def post(self, request):
        data = JSONParser().parse(request)
        serializer = GroupSerializer(data=data)
        if (serializer.is_valid()):
            serializer.save()
            return Response(data=serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(status.HTTP_406_NOT_ACCEPTABLE)

    

# 방 가입, 삭제 클래스
class group_detail(APIView):
    def get_object(self, pk):
        try:
            return Group.objects.get(pk=pk)
        except Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

    def get(self, request, pk):
        group = self.get_object(pk)
        serializer = GroupSerializer(group)
        return Response(data = serializer.data, status = status.HTTP_200_OK)

    def delete(self, request, pk):
        obj = self.get_object(pk)
        obj.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

# 스터디 그룹 가입 함수.
@api_view(['POST'])
def join_group(request):
    if (request.method != 'POST'):
        return Response(status=status.HTTP_400_BAD_REQUEST)
    
    data = JSONParser().parse(request)
    user_id = data['user_id']
    group_id = data['group_id']
    role = data['role']
    try:
        user = User.objects.get(pk=user_id)
        group = Group.objects.get(pk=group_id)
    except:
        return Response(status=status.HTTP_404_NOT_FOUND)
    try:
        num_of_people = group.num_people
        max_of_people = group.max_num_people
        if(num_of_people<max_of_people):
            obj, created = User_Group.objects.update_or_create(user=user, group=group)
            if(created):
                group.num_people += 1
                group.save()

                # 그룹의 Tendency 변경 (가입한 유저들의 평균)
                user_groups = User_Group.objects.filter(group_id = group_id)
                count = 0
                tendency = {'rule':0, 'learning':0, 'numberPeople':0, 'friendship':0, 'environment':0, 'style':0}
                for ug in user_groups:
                    ut = User_Tendency.objects.get(user_id = ug.user)
                    try:
                        tendency['rule'] += ut.rule
                        tendency['learning'] += ut.learning
                        tendency['numberPeople'] += ut.numberPeople
                        tendency['friendship'] += ut.friendship
                        tendency['environment'] += ut.environment
                        tendency['style'] += ut.style
                        count += 1
                    except:
                        print(ut, 'is null')
                if(count > 0):
                    for key in tendency.keys():
                        tendency[key] /= count
                    group.rule = tendency['rule']
                    group.learning = tendency['learning']
                    group.numberPeople = tendency['numberPeople']
                    group.friendship = tendency['friendship']
                    group.environment = tendency['environment']
                    group.style = tendency['style']

                group.save()
    except:
        return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
    return Response(status=status.HTTP_201_CREATED)

class UserGroupList(APIView):
    def get_object(self, user_pk, group_pk):
        try:
            user = User.objects.get(pk= user_pk)
            group = Group.objects.get(pk= group_pk)
        except User.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        return user, group
    
    def get(self, request, user_pk, group_pk):
        user, group = self.get_object(user_pk, group_pk)
        try:
            user_group = User_Group.objects.filter(user = user).get(group = group)
            return Response(data=200, status=status.HTTP_200_OK)
        except User_Group.DoesNotExist:
            return Response(data=404, status=status.HTTP_404_NOT_FOUND)

    def delete(self, request, user_pk, group_pk):
        user, group = self.get_object(user_pk, group_pk)
        try:
            user_group = User_Group.objects.filter(user = user).get(group = group)
        except User_Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        try:
            group = user_group.group
            user_group.delete()
            group.num_people -= 1
            group.save()
            return Response(status=status.HTTP_204_NO_CONTENT)
        except:
            return Response(status=status.HTTP_400_BAD_REQUEST)

        
class UserGroupListUser(APIView):
    def get_object(self, pk):
        try:
            user = User.objects.get(pk = pk)
        except User.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        return user

    def get(self, request, pk):
        user = self.get_object(pk)
        try:
            user_group = User_Group.objects.filter(user = user)
            list_id = []
            for obj in user_group:
                list_id.append(obj.group.id)
            group = Group.objects.filter(pk__in = list_id).order_by("-created_date")
            serializer = GroupSerializer(group, many=True)
            return Response(data=serializer.data, status=status.HTTP_200_OK)
        except User_Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

class album_list(APIView):
    def get(self, request):
        images = Album.objects.all()
        serializer = AlbumSerializer(images, many=True)
        return Response(serializer.data)

    def post(self, request):
        data = JSONParser().parse(request)
        try:
            group = Group.objects.get(pk=data['group_id'])
            user = User.objects.get(pk=data['user_id'])
        except Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        except User.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        
        obj, created = Album.objects.update_or_create(group=group, user=user, image = data['image'])
        return Response(status=status.HTTP_201_CREATED)

# 그룹 검색. 태그로 검색함.
# data = {
# 'searchText' = '프로그래밍 자바 영어',
# }
class GroupSearch(APIView):
    def readDistrict(self):
        f = open('./rest/data/district.csv')
        lines = csv.reader(f)
        districtList = list()
        for line in lines:
            districtList.append(line)
        return districtList

    def post(self, request):
        data = JSONParser().parse(request)
        districtList = self.readDistrict()
        try:
            words = data['searchText'].split()
            words_copy = list(words)
            for word in words_copy:
                for district in districtList:
                    if(word in district):
                        result = [line[2] for line in districtList if district[1] in line]
                        words.extend(result)
            words = list(set(words))
            groups = Group.objects.filter(Q(tag1__in=words)|Q(tag2__in=words)|Q(tag3__in=words)|Q(tag4__in=words)|Q(tag5__in=words))
        except Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = GroupSerializer(groups, many=True)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

class GroupUpdate(APIView):
    def put(self, request, category):
        data = JSONParser().parse(request)
        try:
            group = Group.objects.get(pk=data['group_id'])
        except Group.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        if (category == 'notification'):
            group.notification = data['content']
        elif (category == 'meeting'):
            group.meeting = data['content']

        group.save()
        return Response(status=status.HTTP_200_OK)


class PlannerList(APIView):
    def get(self, request, group_pk):
        year = None
        month = None
        day = None
        group_pk = group_pk
        if 'year' in request.GET:
            year = request.GET['year']
        if 'month' in request.GET:
            month = request.GET['month']
        if 'day' in request.GET:
            day = request.GET['day']
        #if year is None or month is None or day is None:

        planner = Planner.objects.filter(
            group_id=group_pk, date__year=year, date__month=month, date__day=day)
        serializer = PlannerSerializer(planner, many=True)
        return Response(serializer.data)

    def post(self, request):
        data = JSONParser().parse(request)
        serializer = PlannerSerializer(data=data)
        if (serializer.is_valid()):
            serializer.save()
            return Response(data=serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(status.HTTP_406_NOT_ACCEPTABLE)


class PlannerDetail(APIView):
    def get_object(self, pk):
        try:
            return Planner.objects.get(pk=pk)
        except Planner.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

    def delete(self, request, pk):
        obj = self.get_object(pk)
        obj.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

    def put(self, request, pk):
        data = JSONParser().parse(request)
        try:
            planner = self.get_object(pk)
        except Planner.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

        planner.date = data['date']
        planner.title = data['title']
        planner.content = data['content']
        planner.save()
        return Response(status=status.HTTP_200_OK)