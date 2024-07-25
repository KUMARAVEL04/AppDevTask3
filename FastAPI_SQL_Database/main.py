
from fastapi import FastAPI, Depends, HTTPException
from models import Base, User, Task
from schemas import UserSchema, TaskSchema, UserName, TaskWithIdSchema,ReserveSchema
from database import engine,SessionLocal
from sqlalchemy.orm import Session
import uvicorn
import uvicorn



Base.metadata.create_all(bind=engine)

app = FastAPI()




def get_db():
    try:
        db = SessionLocal()
        yield db
    finally:
        db.close()

@app.get("/")
async def home(db: Session = Depends(get_db)):
    users = db.query(User).all()
    return users

@app.get("/karma/{username}")
async def home(username,db: Session = Depends(get_db)):
    karma = db.query(User).filter(User.username==username).first().karma
    return karma

@app.post("/adduser")   
async def add_user(request:UserSchema, db: Session = Depends(get_db)):
    user = User( password = request.password, username = request.username, karma = 50)
    existing_user = db.query(User).filter(User.username == user.username).first()
    print(existing_user)
    if existing_user:
        raise HTTPException(status_code=400, detail="Username already taken")
    db.add(user)
    db.commit()
    db.refresh(user)
    return user

@app.post("/addtask")
async def add_user(request:TaskSchema, db: Session = Depends(get_db)):
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
    
@app.post("/complete")
async def complete_task(request:TaskWithIdSchema,db:Session = Depends(get_db)):
    task = db.query(Task).filter(Task.taskid==request.taskid).filter(Task.underinspection==True).first()
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    reserveduser = db.query(User).filter(User.username == task.reservename).first()
    reserveduser.karma+=task.karma
    db.delete(task)
    db.commit()
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
    users = db.query(User).filter(User.username == request.username).filter(User.password == request.password).first()
    if(users):
        return users
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