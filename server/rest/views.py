from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from rest_framework.decorators import api_view
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from .models import User, Group, Subject, Tendency
from .models import User_Group, User_Subject, User_Tendency
from .models import Wait, Album
from .serializers import UserSerializer, UserSubjectSerializer, UserTendencySerializer
from. serializers import WaitSerializer, GroupSerializer, UserGroupSerializer, AlbumSerializer
from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from django.http import Http404

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
            if (user[0].auth_pw == data['auth_pw']):
                serializer = UserSerializer(user[0])
                return (Response(data=serializer.data, status=status.HTTP_200_OK))
            else:
                return (Response(status=status.HTTP_404_NOT_FOUND))
        except(User.DoesNotExist):
            return (Response(status=status.HTTP_404_NOT_FOUND))

    else:
        return (Response(status = status.HTTP_400_BAD_REQUEST))

@api_view(['GET', 'POST'])
def choice_subject(request):
    if (request.method == 'POST'):
        data = JSONParser().parse(request)
        user_id = data['id']
        list = []

        for key in data:
            if(key == 'id'):
                continue
            try:
                subject_id = (Subject.objects.get(name=key)).id
                if(data[key] == 1):
                    insert = User_Subject.objects.create(user_id=User.objects.get(pk=user_id), subject_id=Subject.objects.get(pk=subject_id))
                    list.append(insert)
                else:
                    queryset = User_Subject.objects.all()
                    queryset = queryset.filter(user_id=User.objects.get(pk=user_id), subject_id=Subject.objects.get(pk=subject_id))
                    queryset.delete()
            except:
                continue
            
        return Response(status=status.HTTP_200_OK)
    
    elif (request.method == 'GET'):
        user_subject = User_Subject.objects.all()
        serializer = UserSubjectSerializer(user_subject, many=True)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    else:
        return Response(status=status.HTTP_404_NOT_FOUND)

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
    except:
        return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
    return Response(status=status.HTTP_201_CREATED)

class UserGroupList(APIView):
    def get_object(self, user_pk, group_pk):
        try:
            print(user_pk, group_pk)
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

