from sqlalchemy import Column, Integer, String, Boolean, DateTime, ForeignKey
from database import Base
from sqlalchemy.orm import relationship

class User(Base):
    __tablename__ = "users"

    userid = Column(Integer, primary_key=True, index=True, autoincrement="auto")
    password = Column(String)
    username = Column(String,unique=True)
    karma = Column(Integer)

    tasks = relationship("Task",backref="owner", foreign_keys="Task.username")

# class Transactions(Base):
#     __tablename__ = "transactions"

#     transactionid = Column(Integer, primary_key=True, index=True)
#     donor = Column(String, ForeignKey("users.username"))
#     reciever = Column(String, ForeignKey("users.username"))
#     taskid = Column(Integer)
#     karma = Column(Integer)
#     date = Column(DateTime)

class Task(Base):
    __tablename__ = "tasks"

    taskid = Column(Integer, primary_key=True, index=True,autoincrement="auto")
    username = Column(String, ForeignKey("users.username"))
    description = Column(String)
    title = Column(String)
    isreserved = Column(Boolean,default=False,nullable=False)
    underinspection = Column(Boolean,default=False,nullable=False)
    karma = Column(Integer)
    reservename = Column(String, ForeignKey("users.username"),nullable=True)



