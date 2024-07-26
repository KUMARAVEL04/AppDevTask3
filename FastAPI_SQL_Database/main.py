from passlib.context import CryptContext
from fastapi import FastAPI, Depends, HTTPException
from models import Base, User, Task, HallFame
from schemas import UserSchema, TaskSchema, UserName, TaskWithIdSchema,ReserveSchema,TaskWithOnlyIdSchema
from database import engine,SessionLocal
from sqlalchemy.orm import Session
import uvicorn
import uvicorn

file1 = open("key.txt","r")
stringkey = file1.readline()
SECRET_KEY = stringkey
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

Base.metadata.create_all(bind=engine)

app = FastAPI()


def hash_password(password: str) -> str:
    return pwd_context.hash(password)

def verify_password(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)




def get_db():
    try:
        db = SessionLocal()
        yield db
    finally:
        db.close()

@app.get("/")
async def home(db: Session = Depends(get_db)):
    return "This is the API for APPDEVTASK3"


@app.get("/allUsers")
async def home(db: Session = Depends(get_db)):
    users = db.query(User).all()
    return users

@app.get("/karma/{username}")
async def home(username,db: Session = Depends(get_db)):
    karma = db.query(User).filter(User.username==username).first().karma
    return karma

@app.get("/karmaadd/{username}")
async def karmadd(username,db: Session = Depends(get_db)):
    karmax = db.query(User).filter(User.username==username).first()
    karmax.karma+=1
    db.commit()
    db.refresh(karmax)
    return karmax.karma

@app.post("/adduser")   
async def add_user(request:UserSchema, db: Session = Depends(get_db)):
    user = User( password = hash_password(password=request.password), username = request.username, karma = 50)
    existing_user = db.query(User).filter(User.username == user.username).first()
    print(existing_user)
    if existing_user:
        raise HTTPException(status_code=400, detail="Username already taken")
    db.add(user)
    db.commit()
    db.refresh(user)
    return user

@app.post("/addtask")
async def add_task(request:TaskSchema, db: Session = Depends(get_db)):
    users = db.query(User).filter(User.username == request.username).first()
    if(users):
        if users.karma < request.karma:
            raise HTTPException(status_code=400, detail="Not enough karma points")
        task = Task(username = request.username,description = request.description,title = request.title,isreserved = False, karma = request.karma)
        users.karma-=request.karma
        db.add(task)
        db.commit()
        db.refresh(users)
        db.refresh(task)
        return task 
    else:
        raise HTTPException(status_code=400, detail="No Such User")
    
@app.post("/updatetask")
async def update_task(request:TaskWithOnlyIdSchema, db: Session = Depends(get_db)):
    users = db.query(User).filter(User.username == request.username).first()
    if(users):
        realtask = db.query(Task).filter(Task.taskid==request.taskid).first()
        if not (realtask.underinspection):
            users.karma+=realtask.karma
            realtask.description=request.description
            realtask.title=request.title
            realtask.karma=request.karma
            if(realtask.isreserved):
                realtask.isedited = True
            users.karma-=request.karma
            db.add(realtask)
            db.commit()
            db.refresh(users)
            db.refresh(realtask)
            return "Task Updated" 
        else:
            raise HTTPException(status_code=400, detail="Task Not Available")

    else:
        raise HTTPException(status_code=400, detail="No Such User")
    
@app.post("/complete")
async def complete_task(request:TaskWithIdSchema,db:Session = Depends(get_db)):
    task = db.query(Task).filter(Task.taskid==request.taskid).filter(Task.underinspection==True).first()
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    reserveduser = db.query(User).filter(User.username == task.reservename).first()
    reserveduser.karma+=task.karma
    hofentry = HallFame(volunteer=request.reservename,owner=request.username,title=request.title,description=request.description,karma=request.karma)
    db.add(hofentry)
    db.delete(task)
    db.commit()
    db.refresh(reserveduser)
    db.refresh(hofentry)
    return "Task {task.taskid} completed successfully"

@app.post("/submit")
async def submit_task(request:ReserveSchema,db:Session = Depends(get_db)):
    task = db.query(Task).filter(Task.taskid==request.taskid).filter(Task.isreserved==True).filter(Task.reservename==request.reservename).first()
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    task.underinspection=True
    db.commit()
    db.refresh(task)
    return "Task {task.taskid} submitted successfully"


@app.post("/login")
async def login_check(request:UserSchema, db:Session = Depends(get_db)):
    users = db.query(User).filter(User.username == request.username).first()

    if(users):
        if(verify_password(hashed_password=users.password,plain_password=request.password)):
            return users
        else:
            raise HTTPException(status_code=400, detail="No Account")

    else:
        raise HTTPException(status_code=400, detail="No Account")

    

@app.get("/othertask/{username}")
async def get_other_tasks(username,db:Session = Depends(get_db)):
    users = db.query(User).filter(User.username == username).first()
    if(users):
        tasks = db.query(Task).filter(Task.username != username).filter(Task.isreserved==False).all()
        return tasks
    else:
        raise HTTPException(status_code=400, detail="No Account")


@app.get("/yourtask/{username}")
async def get_other_tasks(username,db:Session = Depends(get_db)):
    users = db.query(User).filter(User.username == username).first()
    if(users):
        tasks = db.query(Task).filter(Task.reservename == username).filter(Task.isreserved==True).all()
        return tasks
    else:
        raise HTTPException(status_code=400, detail="No Account")
    
    
@app.get("/HOF/{username}")
async def get_prev_tasks(username,db:Session = Depends(get_db)):
    users = db.query(User).filter(User.username == username).first()
    if(users):
        tasks = db.query(HallFame).filter(HallFame.volunteer == username).all()
        if not tasks:
            raise HTTPException(status_code=400, detail="No Previous Completions")
        return tasks
    else:
        raise HTTPException(status_code=400, detail="No Such Account")


@app.get("/task2/{username}")
async def get_tasks(username,db:Session = Depends(get_db)):
    users = db.query(User).filter(User.username == username).first()
    if(users):
        tasks = db.query(User).filter(User.username == username).first().tasks
        return tasks
    else:
        raise HTTPException(status_code=400, detail="No Account")
    
@app.post("/reserve")
async def get_tasks(request:ReserveSchema,db:Session = Depends(get_db)):
    users = db.query(User).filter(User.username == request.reservename).first()
    if(users):
        tasks = db.query(Task).filter(Task.isreserved==False).filter(Task.taskid == request.taskid).first()
        if(tasks):
            tasks.isreserved=True
            tasks.reservename=request.reservename
            db.commit()
            db.refresh(tasks)
        return tasks
    else:
        raise HTTPException(status_code=400, detail="Task Unavilable")
    


if __name__ == '__main__':
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)


    # uvicorn main:app --host 0.0.0.0 --port 8000