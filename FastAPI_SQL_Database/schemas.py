from pydantic import BaseModel
import datetime

class UserSchema(BaseModel):
    username: str
    password: str

    class Config:
        orm_mode = True

class UserName(BaseModel):
    username: str
    password: str

    class Config:
        orm_mode = True



class TaskSchema(BaseModel):
    username: str
    description: str
    title: str
    karma:int

    class Config:
        orm_mode = True

class TaskWithIdSchema(BaseModel):
    username: str
    description: str
    title: str
    karma:int
    taskid: int
    isreserved: bool
    underinspection: bool
    reservename: str


    class Config:
        orm_mode = True

class ReserveSchema(BaseModel):
    taskid:int
    reservename:str
    
    class Config:
        orm_mode = True
    
# class TransactionSchema(BaseModel):
#     donor: str
#     reciever: str
#     taskid: int
#     karma:int
#     date:datetime
 