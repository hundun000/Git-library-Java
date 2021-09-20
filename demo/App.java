/* 
 * Git library
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/git-library-java
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import io.nayuki.git.CommitGraph;
import io.nayuki.git.CommitId;
import io.nayuki.git.CommitObject;
import io.nayuki.git.FileRepository;
import io.nayuki.git.GitObject;
import io.nayuki.git.Reference;
import io.nayuki.git.Repository;


public final class App {
	
	public static void main(String[] args) throws IOException {
//		// Check command line arguments
//		if (args.length < 1) {
//			System.err.println("Usage: java ShowCommitGraphInfo GitDirectory [BranchNames...]");
//			System.exit(1);
//			return;
//		}
	    
	    App app = new App();
	    app.work();
		
		
		
	}
	
	private void work() throws IOException {
	    String repositoryPath = "testRepo/.git";
        String refName = "heads/master";
        long commitTimeOffset = - 1 * 60 * 60; 
        
        try (Repository repo = new FileRepository(new File(repositoryPath))) {
            Reference ref = repo.readReference(refName);
            if (ref == null) {
                throw new IllegalArgumentException("Reference not found: " + refName);
            }
            CommitId targetCommitId = ref.target;
            CommitObject commitObject = (CommitObject) repo.readObject(targetCommitId);
            commitObject.authorTime += commitTimeOffset;
            commitObject.committerTime = commitObject.authorTime;
            commitObject.message = "new message from " + commitObject.message;
            commitAsNewObject(repo, commitObject, "heads/master-modified");
        }
    }
	
	private void commitAsNewObject(Repository repo, CommitObject commitObject, String newReferenceName) throws IOException {
	    // commitObject instance will change id
	    repo.writeObject(commitObject);
	    Reference newReference = new Reference(newReferenceName, commitObject.getId());
	    repo.writeReference(newReference);
	    System.out.println("commitAsNewObject done, " + commitObject.getId() + " set to " + newReferenceName);
    }

	
}
