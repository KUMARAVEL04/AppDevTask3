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
    isedited = Column(Boolean,default=False,nullable=False)

class HallFame(Base):
    __tablename__ = "hall"

    fameid = Column(Integer, primary_key=True, index=True,autoincrement="auto")
    owner = Column(String, ForeignKey("users.username"))
    description = Column(String)
    title = Column(String)
    karma = Column(Integer)
    volunteer = Column(String, ForeignKey("users.username"),nullable=True)



